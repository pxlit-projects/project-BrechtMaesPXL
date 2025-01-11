import {Component, OnDestroy, inject, OnInit} from '@angular/core';
import { AsyncPipe, NgClass } from '@angular/common';

import { Observable, Subscription, tap } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import {ArticleService} from "../../../shared/services/article.service";
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {CookieServicing} from "../../../shared/services/cookie.service";
import {User} from "../../../shared/models/user.model";
import {FormsModule} from "@angular/forms";
import {ReviewArtilceListComponent} from "../review-artilce-list/review-artilce-list.component";

@Component({
  selector: 'app-article-detail',
  imports: [AsyncPipe, FormsModule, ReviewArtilceListComponent],
  templateUrl: './article-detail.component.html',
  standalone: true,
  styles: []
})
export class ArticleDetailComponent  implements OnDestroy, OnInit {
  articleService: ArticleService = inject(ArticleService);
  route: ActivatedRoute = inject(ActivatedRoute);
  id: string = this.route.snapshot.params['id'];
  article$: Observable<ArticleResponse> = this.articleService.getArtilceById(this.id);
  article!: ArticleResponse;
  user: User | null = CookieServicing.getCookie();
  sub!: Subscription;

  ngOnInit(): void {
    this.sub = this.article$.subscribe((article) => {
      this.article = article;
    });
  }

  ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  saveChanges(): void {
    if (!this.user) return;
    this.articleService.updateArticle(this.article, this.user.role).subscribe({
      next: response => console.log('Article updated successfully:', response),
      error: error => console.error('Error updating article:', error),
    });
  }

  markForReview(): void {
    this.articleService.changeArticleStatus(parseInt(this.article.id), 'REVIEW').subscribe({
      next: response=>{
        this.article.statusArticle = 'REVIEW';
      }

    })

  }
  markForDraft(): void {
    this.articleService.changeArticleStatus(parseInt(this.article.id), 'DRAFT').subscribe({
      next: response =>{
        this.article.statusArticle = 'DRAFT';
      }

    })

  }

  publishArticle(): void {
    this.articleService.changeArticleStatus(parseInt(this.article.id), 'PUBLISHED').subscribe({
      next: response =>{
        this.article.statusArticle = 'PUBLISHED';
      }

    })
  }

  canPublish(): boolean {
    return this.article.approvedBy.length > 0;
  }
}
