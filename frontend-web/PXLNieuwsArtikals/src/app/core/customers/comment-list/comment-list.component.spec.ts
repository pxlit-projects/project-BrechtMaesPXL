import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CommentListComponent } from './comment-list.component';
import { CommentService } from '../../../shared/services/comment.service';
import { CookieServicing } from '../../../shared/services/cookie.service';
import { of, throwError } from 'rxjs';
import { Comment } from '../../../shared/models/Comment.model';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

describe('CommentListComponent', () => {
  let component: CommentListComponent;
  let fixture: ComponentFixture<CommentListComponent>;
  let commentServiceMock: jasmine.SpyObj<CommentService>;

  const mockUser = { id: '1', name: 'Test User' };
  const mockComments: Comment[] = [
    { id: 1, content: 'Test comment 1', editorsId: 'User1', articleId: 1 },
    { id: 2, content: 'Test comment 2', editorsId: 'User2', articleId: 1 },
  ];
  beforeEach(() => {
    commentServiceMock = jasmine.createSpyObj('CommentService', [
      'getComments',
      'addComment',
      'updateComment',
      'deleteComment',
    ]);

    CookieServicing.getCookie = jasmine.createSpy().and.returnValue(mockUser);

    TestBed.configureTestingModule({
      imports: [
        FormsModule,
        CommentListComponent, // Import the standalone component here
      ],
      providers: [{ provide: CommentService, useValue: commentServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(CommentListComponent);
    component = fixture.componentInstance;
    component.articleId = '1';
  });


  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should fetch comments if user is logged in', () => {
      commentServiceMock.getComments.and.returnValue(of(mockComments));
      fixture.detectChanges();

      expect(commentServiceMock.getComments).toHaveBeenCalledWith('1');
      expect(component.filteredData).toEqual(mockComments);
    });

    it('should not fetch comments if user is not logged in', () => {
      CookieServicing.getCookie = jasmine.createSpy().and.returnValue(null);
      component.user = null;

      component.ngOnInit();

      expect(commentServiceMock.getComments).not.toHaveBeenCalled();
    });
  });

  describe('addComment', () => {
    it('should add a comment successfully', () => {
      component.newComments['1'] = 'New comment';
      commentServiceMock.addComment.and.returnValue(of(mockComments[0])); // Return a valid Comment object

      component.addComment();


      expect(component.newComments['1']).toBe('');
    });

    it('should not add a comment if user is not logged in', () => {
      CookieServicing.getCookie = jasmine.createSpy().and.returnValue(null);
      component.user = null;

      spyOn(window, 'alert');
      component.addComment();

      expect(commentServiceMock.addComment).not.toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith('User not logged in');
    });

    it('should not add a comment if the comment is empty', () => {
      component.newComments['1'] = '';
      spyOn(window, 'alert');
      component.addComment();

      expect(commentServiceMock.addComment).not.toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith('Comment cannot be empty');
    });
  });
  describe('saveEdit', () => {
    it('should save an edited comment successfully', () => {
      component.editingCommentId = 1;
      component.editCommentContent = 'Updated comment';
      component.filteredData = mockComments;

      // Mock updateComment correctly
      commentServiceMock.updateComment.and.returnValue(of(mockComments[0])); // Return a valid Comment object

      component.saveEdit(mockComments[0]);

      expect(commentServiceMock.updateComment).toHaveBeenCalledWith(
        jasmine.objectContaining({
          id: 1,
          content: 'Updated comment',
        })
      );

      expect(component.editingCommentId).toBeNull();
    });

    it('should not save if the edited content is empty', () => {
      component.editingCommentId = 1;
      component.editCommentContent = '';
      spyOn(window, 'alert');
      component.saveEdit(mockComments[0]);

      expect(commentServiceMock.updateComment).not.toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith('Comment cannot be empty');
    });
  });




  describe('Edge cases and error handling', () => {
    it('should handle errors when fetching comments', () => {
      commentServiceMock.getComments.and.returnValue(throwError(() => new Error('Error')));
      spyOn(console, 'error');

      component.fetchData();

      expect(console.error).toHaveBeenCalledWith('Error fetching comments:', jasmine.any(Error));
    });

    it('should handle errors when adding a comment', () => {
      component.newComments['1'] = 'New comment';
      commentServiceMock.addComment.and.returnValue(throwError(() => new Error('Error')));
      spyOn(console, 'error');

      component.addComment();

      expect(console.error).toHaveBeenCalledWith('Error adding comment:', jasmine.any(Error));
    });

    it('should handle errors when updating a comment', () => {
      component.editingCommentId = 1;
      component.editCommentContent = 'Updated comment';
      commentServiceMock.updateComment.and.returnValue(throwError(() => new Error('Error')));
      spyOn(console, 'error');

      component.saveEdit(mockComments[0]);

      expect(console.error).toHaveBeenCalledWith('Error updating comment:', jasmine.any(Error));
    });

    it('should handle errors when deleting a comment', () => {
      commentServiceMock.deleteComment.and.returnValue(throwError(() => new Error('Error')));
      spyOn(console, 'error');

      component.deleteComment(1);

      expect(console.error).toHaveBeenCalledWith('Error deleting comment:', jasmine.any(Error));
    });
  });
});
