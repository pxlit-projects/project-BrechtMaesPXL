import { of, throwError } from "rxjs";
import { AddArticleComponent } from "./add-article.component";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { Router } from "@angular/router";
import { CookieService } from "ngx-cookie-service";
import { ArticleService } from "../../../shared/services/article.service";
import { ReactiveFormsModule } from "@angular/forms";
import { CookieServicing } from "../../../shared/services/cookie.service";

describe('AddArticleComponent', () => {
  let component: AddArticleComponent;
  let fixture: ComponentFixture<AddArticleComponent>;
  let postServiceMock: jasmine.SpyObj<ArticleService>;
  let cookieServiceMock: jasmine.SpyObj<CookieService>;
  let routerMock: jasmine.SpyObj<Router>;

  beforeEach(() => {
    postServiceMock = jasmine.createSpyObj('ArticleService', ['addArticle', 'getArticlesByStatus']);
    cookieServiceMock = jasmine.createSpyObj('CookieService', ['set', 'get', 'delete']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);
    postServiceMock.addArticle.and.returnValue(of({})); // Voeg een standaardwaarde toe

    cookieServiceMock.get.and.returnValue(JSON.stringify({ id: "1", role: "editor" }));
    CookieServicing.setInstance(cookieServiceMock);

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      providers: [
        { provide: ArticleService, useValue: postServiceMock },
        { provide: CookieService, useValue: cookieServiceMock },
        { provide: Router, useValue: routerMock },
      ],
    });

    fixture = TestBed.createComponent(AddArticleComponent);
    component = fixture.componentInstance;
  });

  it('should create the form with required controls and validators', () => {
    expect(component.articleForm).toBeTruthy();
    expect(component.articleForm.controls['title'].valid).toBeFalse();
    expect(component.articleForm.controls['content'].valid).toBeFalse();
    expect(component.articleForm.controls['status'].value).toBe('');
  });

  it('should navigate to login if no role is found', () => {
    cookieServiceMock.get.and.returnValue("");

    component.onSubmit();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should call addArticle with valid data and reset form on success', () => {
    const mockResponse = { success: true };
    postServiceMock.addArticle.and.returnValue(of(mockResponse));

    component.articleForm.setValue({ title: 'Test Title', content: 'Test Content', status: 'Draft' });
    component.onSubmit();

    expect(postServiceMock.addArticle).toHaveBeenCalledWith(jasmine.objectContaining({
      title: 'Test Title',
      content: 'Test Content',
      status: 'Draft',
      editorsId: '1'
    }), 'editor');

    expect(component.successMessage).toBe(null);
    expect(component.articleForm.valid).toBeTruthy();
  });

  it('should handle errors gracefully and set errorMessage', () => {
    const mockError = { errorMessage: 'An error occurred' };
    postServiceMock.addArticle.and.returnValue(throwError(() => mockError));

    component.articleForm.setValue({ title: 'Test Title', content: 'Test Content', status: 'Draft' });
    component.onSubmit();

    expect(postServiceMock.addArticle).toHaveBeenCalled();
    expect(component.errorMessage).toBe('An error occurred');
  });
  it('should call addArticle with valid data and reset form on success', () => {
    const mockResponse = { success: true };
    postServiceMock.addArticle.and.returnValue(of(mockResponse));

    component.articleForm.setValue({ title: 'Test Title', content: 'Test Content', status: 'Draft' });
    component.articleForm.markAllAsTouched()

    component.onSubmit();

    expect(postServiceMock.addArticle).toHaveBeenCalledWith(jasmine.objectContaining({
      title: 'Test Title',
      content: 'Test Content',
      status: 'Draft',
      editorsId: '1'
    }), 'editor');

    expect(component.successMessage).toBe(null);
    expect(component.articleForm.valid).toBeTruthy();
  });

});
