import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ArticleDetailComponent } from './article-detail.component';
import { ArticleService } from '../../../shared/services/article.service';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { CookieServicing } from '../../../shared/services/cookie.service';
import { ArticleResponse } from '../../../shared/models/ArticleRsponse.model';
import { User } from '../../../shared/models/user.model';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ArticleDetailComponent', () => {
  let component: ArticleDetailComponent;
  let fixture: ComponentFixture<ArticleDetailComponent>;
  let articleServiceMock: jasmine.SpyObj<ArticleService>;
  let routeMock: any;
  let mockArticle: ArticleResponse;
  let mockUser: User;


  beforeEach(() => {
    articleServiceMock = jasmine.createSpyObj('ArticleService', [
      'getArtilceById',
      'updateArticle',
      'changeArticleStatus',
    ]);

    mockArticle = {
      id: '1',
      title: 'Test Article',
      content: 'Test Content',
      approvedBy: [],
      statusArticle: 'DRAFT',
      rejectedBy: [],
      notification: 0,
      createdAt: '' + new Date(),
      editorsId: '1',
    };

    mockUser = { id: '1', name: 'Test User', password: 'password', role: 'editor' };

    routeMock = {
      snapshot: {
        params: { id: '1' },
      },
    };

    articleServiceMock.getArtilceById.and.returnValue(of(mockArticle));
    CookieServicing.getCookie = jasmine.createSpy().and.returnValue(mockUser);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Add HttpClientTestingModule
      providers: [
        { provide: ArticleService, useValue: articleServiceMock },
        { provide: ActivatedRoute, useValue: routeMock },
      ],
    });

    fixture = TestBed.createComponent(ArticleDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch article on initialization', () => {
    expect(component.articleService.getArtilceById).toHaveBeenCalledWith('1');
    expect(component.article).toEqual(mockArticle);
  });

  it('should unsubscribe on destroy', () => {
    spyOn(component.sub, 'unsubscribe');
    component.ngOnDestroy();
    expect(component.sub.unsubscribe).toHaveBeenCalled();
  });

  it('should save changes when user is logged in', () => {
    articleServiceMock.updateArticle.and.returnValue(of());
    component.saveChanges();
    expect(articleServiceMock.updateArticle).toHaveBeenCalledWith(mockArticle, mockUser.role);
  });

  it('should not save changes when no user is logged in', () => {
    CookieServicing.getCookie = jasmine.createSpy().and.returnValue(null);
    component.user = null;
    component.saveChanges();
    expect(articleServiceMock.updateArticle).not.toHaveBeenCalled();
  });

  it('should mark article for review', () => {
    articleServiceMock.changeArticleStatus.and.returnValue(of(mockArticle));
    component.markForReview();
    expect(articleServiceMock.changeArticleStatus).toHaveBeenCalledWith(1, 'REVIEW');
    expect(component.article.statusArticle).toBe('REVIEW');
  });

  it('should mark article as draft', () => {
    articleServiceMock.changeArticleStatus.and.returnValue(of(mockArticle));
    component.markForDraft();
    expect(articleServiceMock.changeArticleStatus).toHaveBeenCalledWith(1, 'DRAFT');
    expect(component.article.statusArticle).toBe('DRAFT');
  });

  it('should publish article', () => {
    articleServiceMock.changeArticleStatus.and.returnValue(of(mockArticle));
    component.publishArticle();
    expect(articleServiceMock.changeArticleStatus).toHaveBeenCalledWith(1, 'PUBLISHED');
    expect(component.article.statusArticle).toBe('PUBLISHED');
  });

  it('should return true for canPublish when approvedBy is not empty', () => {
    component.article.approvedBy = ['Approver 1'];
    expect(component.canPublish()).toBeTrue();
  });

  it('should return false for canPublish when approvedBy is empty', () => {
    component.article.approvedBy = [];
    expect(component.canPublish()).toBeFalse();
  });
});
