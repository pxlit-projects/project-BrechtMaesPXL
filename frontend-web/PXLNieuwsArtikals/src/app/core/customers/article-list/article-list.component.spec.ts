import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FilterComponent } from '../filter/filter.component';
import { ArticleItemComponent } from "../article-item/article-item.component";
import { ArticleService } from "../../../shared/services/article.service";
import { ArticleListComponent } from "./article-list.component";
import { ArticleResponse } from "../../../shared/models/ArticleRsponse.model";
import { ActivatedRoute } from "@angular/router";
import { CookieService } from 'ngx-cookie-service';
import { CookieServicing } from "../../../shared/services/cookie.service";

describe('ArticleListComponent', () => {
  let component: ArticleListComponent;
  let fixture: ComponentFixture<ArticleListComponent>;
  let postServiceMock: jasmine.SpyObj<ArticleService>;
  let cookieServiceMock: jasmine.SpyObj<CookieService>;

  let newDate = new Date();

  const articleResponses: ArticleResponse[] = [
    new ArticleResponse("0", 'Article 1', 'Content 1', 'POSTED', "1", "" + newDate, [], [], 0),
    new ArticleResponse("1", 'Article 2', 'Content 2', 'POSTED', "1", "" + newDate, [], [], 0),
    new ArticleResponse("2", 'Article 3', 'Content 3', 'POSTED', "1", "" + newDate, [], [], 0)
  ];

  beforeEach(() => {
    postServiceMock = jasmine.createSpyObj('ArticleService', ['getArticlesByStatus']);

    cookieServiceMock = jasmine.createSpyObj('CookieService', ['set', 'get', 'delete']);
    cookieServiceMock.get.and.returnValue('');
    CookieServicing.setInstance(cookieServiceMock);

    TestBed.configureTestingModule({
      imports: [
        CommonModule,
        ArticleListComponent,
        FilterComponent,
        ArticleItemComponent
      ],
      providers: [
        { provide: ArticleService, useValue: postServiceMock },
        { provide: ActivatedRoute, useValue: { params: of({ id: '1' }) } },
        { provide: CookieService, useValue: cookieServiceMock }
      ]
    });

    fixture = TestBed.createComponent(ArticleListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get posted posts on init', async () => {

    postServiceMock.getArticlesByStatus.and.returnValue(of(articleResponses));

    await component.ngOnInit();

    expect(postServiceMock.getArticlesByStatus).toHaveBeenCalled();
    expect(component.articles).toEqual(articleResponses);
  });

  it('should filter posts based on filter criteria', () => {
    component.articles = articleResponses;
    const filter = {
      content: 'Content 1',
      editorsId: '',
      date: null
    };

    component.handleFilter(filter);

    expect(component.filteredPosts.length).toBe(1);
    expect(component.filteredPosts[0].title).toBe('Article 1');
  });

  it('should toggle comments visibility', () => {
    const title = 'Article 1';

    expect(component.showComments[title]).toBeUndefined();

    component.toggleComments(title);

    expect(component.showComments[title]).toBeTrue();

    component.toggleComments(title);

    expect(component.showComments[title]).toBeFalse();
  });


  it('should handle cookie fetching on initialization', () => {
    expect(component.user).toBe(null);
  });


});
