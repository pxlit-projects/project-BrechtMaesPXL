import {Component, inject, Input, OnInit} from '@angular/core';
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {ArticleService} from "../../../shared/services/article.service";
import {User} from "../../../shared/models/user.model";
import {CookieServicing} from "../../../shared/services/cookie.service";
import {ReviewModel} from "../../../shared/models/review.model";
import {ReveiwService} from "../../../shared/services/reveiw.service";
import {ReviewArtilceItemComponent} from "../review-artilce-item/review-artilce-item.component";

@Component({
  selector: 'app-review-artilce-list',
  imports: [
    ReviewArtilceItemComponent
  ],
  templateUrl: './review-artilce-list.component.html',
  standalone: true,
  styleUrl: './review-artilce-list.component.css'
})
export class ReviewArtilceListComponent implements OnInit{
  @Input() articleId!: string;

  reviewModels!: ReviewModel[];
  reveiwService: ReveiwService = inject(ReveiwService);
  user: User | null = CookieServicing.getCookie();

  ngOnInit(): void {
    this.fetchData()

  }


  fetchData(): void {
    if (!this.user) return;
  this.reveiwService.getReviews(this.articleId).subscribe({
    next: data => {
      this.reviewModels = data;
    }
  })
}
}
