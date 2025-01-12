import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ArticleService } from "../../../shared/services/article.service";
import { CookieService } from "ngx-cookie-service";
import { ArticleResponse } from "../../../shared/models/ArticleRsponse.model";
import { CookieServicing } from "../../../shared/services/cookie.service";
import { CommonModule } from "@angular/common";
import { FilterComponent } from "../filter/filter.component";
import { ArticleItemComponent } from "./article-item.component";
import { ActivatedRoute, provideRouter } from "@angular/router";
import { of } from "rxjs";
import { NO_ERRORS_SCHEMA } from "@angular/core";
import { By } from "@angular/platform-browser";

describe('ArticleItemComponent', () => {
  let component: ArticleItemComponent;
  let fixture: ComponentFixture<ArticleItemComponent>;
  let postServiceMock: jasmine.SpyObj<ArticleService>;
  let cookieServiceMock: jasmine.SpyObj<CookieService>;

  let newDate = new Date();

  const articleResponse: ArticleResponse =
    new ArticleResponse("0", 'Article 1', 'Content 1', 'POSTED', "1", "" + newDate, [], [], 1);

  beforeEach(() => {
    postServiceMock = jasmine.createSpyObj('ArticleService', ['getArticlesByStatus', 'updateNotification']);
    cookieServiceMock = jasmine.createSpyObj('CookieService', ['set', 'get', 'delete']);
    cookieServiceMock.get.and.returnValue('');
    CookieServicing.setInstance(cookieServiceMock);

    TestBed.configureTestingModule({
      imports: [
        CommonModule,
        ArticleItemComponent,
        FilterComponent,
      ],
      providers: [
        { provide: ArticleService, useValue: postServiceMock },
        { provide: ActivatedRoute, useValue: { params: of({ id: '1' }) } },
        { provide: CookieService, useValue: cookieServiceMock },
        provideRouter([]),
      ],
      schemas: [NO_ERRORS_SCHEMA]
    });

    fixture = TestBed.createComponent(ArticleItemComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should render article name in the template', () => {
    component.article = articleResponse;
    fixture.detectChanges();

    const debugElement = fixture.debugElement.query(By.css('h2'));
    expect(debugElement.nativeElement.textContent).toContain('Article 1');
  });

  it('should call updateNotification when notification is clicked', () => {
    component.article = articleResponse;
    fixture.detectChanges();

    const notificationButton = fixture.debugElement.query(By.css('button'));

    expect(notificationButton).toBeTruthy();

    postServiceMock.updateNotification.and.returnValue(of({}));
    notificationButton.nativeElement.click();

    expect(postServiceMock.updateNotification).toHaveBeenCalledWith(0);
  });
  it('should display the notification badge if notification > 0', () => {
    component.article = {
      id: '1',
      title: 'Article 1',
      content: 'Content 1',
      statusArticle: 'POSTED',
      editorsId: '1',
      createdAt: '' + new Date(),
      approvedBy: [],
      rejectedBy: [],
      notification: 2 // Zet de notificatie op een waarde groter dan 0
    };

    fixture.detectChanges();

    const badge = fixture.debugElement.query(By.css('.notification-badge'));

    expect(badge).toBeTruthy();
    expect(badge.nativeElement.textContent).toContain('2'); // Controleer of de notificatiewaarde juist is
  });
  it('should not display the notification badge if notification <= 0', () => {
    component.article = {
      id: '1',
      title: 'Article 1',
      content: 'Content 1',
      statusArticle: 'POSTED',
      editorsId: '1',
      createdAt: '' + new Date(),
      approvedBy: [],
      rejectedBy: [],
      notification: 0 // Zet de notificatie op 0
    };

    fixture.detectChanges();

    const badge = fixture.debugElement.query(By.css('.notification-badge'));

    expect(badge).toBeNull(); // Het badge-element zou niet moeten bestaan
  });

});
