import { TestBed } from "@angular/core/testing";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { ArticleService } from "./article.service";
import { provideHttpClient } from "@angular/common/http";
import { ArticleTest } from "../models/ArticleTest.model";
import {ArticleResponse} from "../models/ArticleRsponse.model";

describe('ArticleService', () => {
  let service: ArticleService;
  let httpTestingController: HttpTestingController;

  const mockArticles: ArticleResponse[] = [
    new ArticleResponse("1", "Article 1", "Content 1", "POSTED", "1", "2025-01-01", [], [], 0),
    new ArticleResponse("2", "Article 2", "Content 2", "POSTED", "1", "2025-01-02", [], [], 0)
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ArticleService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    });

    service = TestBed.inject(ArticleService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should add an article via POST', () => {
    const newArticle: ArticleTest = { title: 'New Article', content: 'Content', statusArticle: 'Draft', editorsId: '1' };

    service.addArticle(newArticle, 'editor').subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpTestingController.expectOne(service.api);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('role')).toBe('editor');
    req.flush({});
  });

  it('should fetch articles by status via GET', () => {
    service.getArticlesByStatus('POSTED').subscribe(articles => {
      expect(articles).toEqual(mockArticles);
    });

    const req = httpTestingController.expectOne(`${service.api}/POSTED`);
    expect(req.request.method).toBe('GET');
    req.flush(mockArticles);
  });

  it('should update an article via PUT', () => {
    const updatedArticle: ArticleResponse = { ...mockArticles[0], title: 'Updated Title' };

    service.updateArticle(updatedArticle, 'editor').subscribe(() => {
      expect(true).toBeTrue(); // Ensure success
    });

    const req = httpTestingController.expectOne(`${service.api}/update/${updatedArticle.id}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.headers.get('role')).toBe('editor');
    expect(req.request.body).toEqual(updatedArticle);
    req.flush({});
  });

  it('should retrieve an article by ID via GET', () => {
    const articleId = "1";

    service.getArtilceById(articleId).subscribe(article => {
      expect(article).toEqual(mockArticles[0]);
    });

    const req = httpTestingController.expectOne(`${service.api}/id/${articleId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockArticles[0]);
  });
});
