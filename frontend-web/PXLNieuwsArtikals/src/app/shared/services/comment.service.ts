import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ReviewModel} from "../models/review.model";
import {Comment} from "../models/Comment.model";

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  api: string = environment.backendUrl + "comment/api/comment";
  http: HttpClient = inject(HttpClient);

  getComments(id: string): Observable<Comment[]>
  {
    return this.http.get<Comment[]>(`${this.api}/post/${id}`);
  }
  addComment(comment: Comment){
    return this.http.post<Comment>(`${this.api}`, comment);
  }
}