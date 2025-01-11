import {inject, Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ReviewModel} from "../models/review.model";
import {RequestComment} from "../models/RequestComment";

@Injectable({
  providedIn: 'root'
})
export class ReveiwService {

  api: string = environment.backendUrl + "review/api/review";
  http: HttpClient = inject(HttpClient);

  getReviews(id: string): Observable<ReviewModel[]>
  {
    return this.http.get<ReviewModel[]>(`${this.api}/post/${id}`);
  }
  addReview(reviewRequest: RequestComment)
  {
    return this.http.post<ReviewModel>(`${this.api}`, reviewRequest);
  }
}
