import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import {Article} from "../models/Article.model";
import {catchError, empty, map, Observable, throwError} from "rxjs";
import {ArticleResponse} from "../models/ArticleRsponse.model";
import {Filter} from "../models/filter.model";
import {ArticleTest} from "../models/ArticleTest.model";

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  api: string = environment.backendUrl + "post/api/article";
  http: HttpClient = inject(HttpClient);

  addArticle(article: ArticleTest, role: string): Observable<Object> {
    const headers = { role };
    console.log("Sending article:", article);

    return this.http.post(`${this.api}`, article, {
      headers: {
        role,
        'Content-Type': 'application/json'
      },
      withCredentials: true
    }).pipe(
      catchError(this.handleError)

    );
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
    ).pipe(
     catchError(this.handleError)

   );
  }
  getArticlesOfEditorByStatus( status: string, editorId: string ): Observable<ArticleResponse[]> {
    return this.http.get<ArticleResponse[]>(`${this.api}/${status}/${editorId}`).pipe(
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
    ).pipe(
      catchError(this.handleError)

    );
  }
  getArtilceById(id: string): Observable<ArticleResponse> {
    return this.http.get<ArticleResponse>(`${this.api}/id/${id}`).pipe(
      catchError(this.handleError)

    );
  }

  updateArticle(article: ArticleResponse, role:string): Observable<void> {
    const headers = { role };
    return this.http.put<void>(`${this.api}/${article.id}`, article, { headers }).pipe(
      catchError(this.handleError)

    );
  }
  changeArticleStatus(id: number, status: string): Observable<ArticleResponse> {
    return this.http.put<ArticleResponse>(`${this.api}/${id}/status/${status}`, null, {
      withCredentials: false,
      headers: {
        'Content-Type': 'application/json'
      }
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage: string = "Unknown error";
    if (error.error instanceof ErrorEvent) {
      errorMessage = `${error.error.message }`;
    } else {
      errorMessage = `unexpected error: ${error.message}`;

    }
    return throwError(() => new Error(errorMessage));
  }



}
