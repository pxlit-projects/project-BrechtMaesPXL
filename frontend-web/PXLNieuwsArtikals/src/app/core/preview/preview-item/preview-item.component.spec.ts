import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PreviewItemComponent } from './preview-item.component';
import { ReveiwService } from '../../../shared/services/reveiw.service';
import { Router } from '@angular/router';
import { CookieServicing } from '../../../shared/services/cookie.service';
import { of, throwError } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { CookieService } from 'ngx-cookie-service';
import { ArticleResponse } from '../../../shared/models/ArticleRsponse.model';

describe('PreviewItemComponent', () => {
  let component: PreviewItemComponent;
  let fixture: ComponentFixture<PreviewItemComponent>;
  let reviewServiceMock: jasmine.SpyObj<ReveiwService>;
  let routerMock: jasmine.SpyObj<Router>;
  let cookieServiceMock: jasmine.SpyObj<CookieService>;

  beforeEach(() => {
    reviewServiceMock = jasmine.createSpyObj('ReveiwService', ['addReview']);
    routerMock = jasmine.createSpyObj('Router', ['navigate']);
    cookieServiceMock = jasmine.createSpyObj('CookieService', ['set', 'get', 'delete']);

    cookieServiceMock.get.and.returnValue(
      JSON.stringify({ id: '1', role: 'editor' })
    );
    CookieServicing.setInstance(cookieServiceMock);

    // Mock returned value with the correct type and fields
    reviewServiceMock.addReview.and.returnValue(
      of({
        editorsId: 'Test User',
        postId: "1",
        type: 'ACCEPTED',
        title: 'Test Title',
        content: 'Test Content',
      })
    );

    TestBed.configureTestingModule({
      imports: [RouterTestingModule, FormsModule, PreviewItemComponent],
      providers: [
        { provide: ReveiwService, useValue: reviewServiceMock },
        { provide: Router, useValue: routerMock },
      ],
    });

    fixture = TestBed.createComponent(PreviewItemComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to login if no user is found', () => {
    CookieServicing.removeCookie();
    spyOn(CookieServicing, 'getCookie').and.returnValue(null);
    component.onSubmit();

  });


});
