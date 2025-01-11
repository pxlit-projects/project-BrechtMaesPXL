import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ReviewModel} from "../models/review.model";
import {Comment} from "../models/Comment.model";
import {RequestComment} from "../models/RequestComment";

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
    var newRequestComment: RequestComment  = new RequestComment(comment.content,comment.editorsId, comment.articleId )
    return this.http.post<Comment>(`${this.api}`, newRequestComment);
  }

  updateComment(comment: Comment) {
    return this.http.put<Comment>(`${this.api}/${comment.id}`, comment);
  }

  deleteComment(commentId: number) {
    return this.http.delete(`${this.api}/${commentId}`);
  }
}
