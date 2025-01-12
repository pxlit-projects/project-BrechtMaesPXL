import { TestBed } from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { CommentService } from './comment.service';
import { environment } from '../../../environments/environment';
import { Comment } from '../models/Comment.model';
import { RequestComment } from '../models/RequestComment';

describe('CommentService', () => {
  let service: CommentService;
  let httpTestingController: HttpTestingController;

  const mockComments: Comment[] = [
    { id: 1, content: 'Great article!', editorsId: '1', articleId: 1 },
    { id: 2, content: 'Interesting read!', editorsId: '1', articleId: 1 },
  ];

  const newComment: Comment = { id: 3, content: 'Nice article!', editorsId: '2', articleId: 2 };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Import HttpClientTestingModule here
      providers: [CommentService],
    });

    service = TestBed.inject(CommentService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify(); // Verifies that there are no outstanding HTTP requests
  });


  afterEach(() => {
    httpTestingController.verify();
  });

  it('should retrieve comments by post ID', () => {
    const postId = '1';

    service.getComments(postId).subscribe((comments) => {
      expect(comments).toEqual(mockComments);
    });

    const req = httpTestingController.expectOne(`${service.api}/post/${postId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockComments);
  });

  it('should add a new comment', () => {
    const requestComment: RequestComment = new RequestComment(newComment.content, newComment.editorsId, newComment.articleId);

    service.addComment(newComment).subscribe((comment) => {
      expect(comment).toEqual(newComment);
    });

    const req = httpTestingController.expectOne(service.api);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(requestComment);
    req.flush(newComment);
  });

  it('should update a comment', () => {
    const updatedComment: Comment = { ...mockComments[0], content: 'Updated content' };

    service.updateComment(updatedComment).subscribe((comment) => {
      expect(comment).toEqual(updatedComment);
    });

    const req = httpTestingController.expectOne(`${service.api}/${updatedComment.id}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedComment);
    req.flush(updatedComment);
  });

  it('should delete a comment', () => {
    const commentId = 2;

    service.deleteComment(commentId).subscribe(() => {
      expect(true).toBeTrue(); // Ensure success
    });

    const req = httpTestingController.expectOne(`${service.api}/${commentId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
