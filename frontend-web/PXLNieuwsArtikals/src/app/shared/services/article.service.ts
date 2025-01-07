import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from '@angular/common/http';
import {Article} from "../models/Article.model";
import {map, Observable} from "rxjs";
import {ArticleResponse} from "../models/ArticleRsponse.model";

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  api: string = environment.backendUrl + "post/api/article";
  http: HttpClient = inject(HttpClient);

  addArticle(article: Article, role: string): Observable<Object> {
    const headers = { role }; // Simplified object
    console.log("Sending article:", article);

    return this.http.post(`${this.api}`, article, {
      headers: {
        role,
        'Content-Type': 'application/json' // Explicit content type
      },
      withCredentials: true // Ensure credentials are sent if needed
    });
  }

  getArticlesByStatus(status: string): Observable<ArticleResponse[]> {
    console.log("Requesting articles by status: " + status);
   return this.http.get<ArticleResponse[]>(`${this.api}/${status}`).pipe(
      map((response: any[]) => {
        return response.map(article => {
          return new ArticleResponse(
            article.id,
            article.title,
            article.content,
            article.statusArticle,
            article.editorsId,
            article.createdAt,
            article.approvedBy
          );
        });
      })
    );
  }

  updateArticle(id: number, article: Article, role: string): Observable<void> {
    const headers = { role };
    return this.http.put<void>(`${this.api}/${id}`, article, { headers });
  }

  changeArticleStatus(id: number, status: string): Observable<void> {
    return this.http.put<void>(`${this.api}/${id}/status`, status);
  }

  getArticlesWithFilter(content?: string, editorsId?: number, date?: string): Observable<Article[]> {
    const params: any = { content, editorsId, date };
    return this.http.get<Article[]>(`${this.api}/filter`, { params });
  }
}
