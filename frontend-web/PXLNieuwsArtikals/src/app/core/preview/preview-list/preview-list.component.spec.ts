import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { CommonModule } from '@angular/common';
import { PreviewListComponent } from './preview-list.component';
import { PreviewItemComponent } from '../preview-item/preview-item.component';
import { ArticleService } from "../../../shared/services/article.service";
import { ArticleResponse } from "../../../shared/models/ArticleRsponse.model";
import { CookieServicing } from "../../../shared/services/cookie.service";
import { CookieService } from 'ngx-cookie-service';

describe('PreviewListComponent', () => {
  let component: PreviewListComponent;
  let fixture: ComponentFixture<PreviewListComponent>;
  let articleServiceMock: jasmine.SpyObj<ArticleService>;
  let cookieServiceMock: jasmine.SpyObj<CookieService>;

  const articleResponses: ArticleResponse[] = [
    new ArticleResponse("0", 'Article 1', 'Content 1', 'REVIEW', "1", "2025-01-14T12:00:00", [], [], 0),
    new ArticleResponse("1", 'Article 2', 'Content 2', 'REVIEW', "1", "2025-01-14T12:00:00", [], [], 0),
    new ArticleResponse("2", 'Article 3', 'Content 3', 'REVIEW', "1", "2025-01-14T12:00:00", [], [], 0)
  ];

  beforeEach(() => {
    articleServiceMock = jasmine.createSpyObj('ArticleService', ['getArticlesOfEditorByStatus']);
    cookieServiceMock = jasmine.createSpyObj('CookieService', ['get']);

    cookieServiceMock.get.and.returnValue('{"id": "1", "role": "editor"}');
    CookieServicing.setInstance(cookieServiceMock);

    articleServiceMock.getArticlesOfEditorByStatus.and.returnValue(of(articleResponses));

    TestBed.configureTestingModule({
      imports: [CommonModule, PreviewListComponent, PreviewItemComponent],
      providers: [
        { provide: ArticleService, useValue: articleServiceMock },
        { provide: CookieService, useValue: cookieServiceMock }
      ]
    });

    fixture = TestBed.createComponent(PreviewListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch data on init when user is present', () => {
    component.ngOnInit();

    expect(articleServiceMock.getArticlesOfEditorByStatus).toHaveBeenCalledWith("REVIEW", "1");

    expect(component.filteredData).toEqual(articleResponses);
  });


});
