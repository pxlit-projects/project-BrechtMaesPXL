import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../../shared/models/user.model";
import {ArticleService} from "../../../shared/services/article.service";
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {Filter} from "../../../shared/models/filter.model";
import {FilterComponent} from "../filter/filter.component";
import { FormsModule} from "@angular/forms";
import {Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {ReviewArtilceCommentItemComponent} from "../review-artilce-comment-item/review-artilce-comment-item.component";
import {CookieServicing} from "../../../shared/services/cookie.service";
import {CommentListComponent} from "../comment-list/comment-list.component";

@Component({
  selector: 'app-article-list',
  imports: [
    FilterComponent,
    ReviewArtilceCommentItemComponent,
    FormsModule,
    CommentListComponent

  ],
  templateUrl: './article-list.component.html',
  standalone: true,
  styleUrl: './article-list.component.css'
})
export class ArticleListComponent implements OnInit, OnDestroy {
  articles: ArticleResponse[] = [];
  filteredPosts: ArticleResponse[] = [];
  showComments: { [key: string]: boolean } = {};
  user: User | null = CookieServicing.getCookie();

  private routeParamSubscription: Subscription | undefined;

  constructor(
    private articleService: ArticleService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.articleService.getArticlesByStatus("PUBLISHED").subscribe({
      next: (response) => {
        this.articles = response;
        this.filteredPosts = this.articles;
      },
      error: (error) => {
        console.error('Error fetching posts:', error);
      }
    });
  }

  toggleComments(title: string): void {
    this.showComments[title] = !this.showComments[title];
  }

 handleFilter(filter: Filter): void {
    this.filteredPosts = this.articles.filter(post => {
      return (
        (!filter.content || post.content.toLowerCase().includes(filter.content.toLowerCase())) &&
        (!filter.editorsId || post.editorsId.toLowerCase().includes(filter.editorsId.toLowerCase())) &&
        (!filter.date || new Date(post.createdAt).toLocaleDateString() === new Date(filter.date).toLocaleDateString())
      );
    });
  }



  ngOnDestroy(): void {
    if (this.routeParamSubscription) {
      this.routeParamSubscription.unsubscribe();
    }
  }
}

