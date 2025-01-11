import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../../shared/models/user.model";
import {UserService} from "../../../shared/services/user.service";
import {ArticleService} from "../../../shared/services/article.service";
import {Article} from "../../../shared/models/Article.model";
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {ArticleItemComponent} from "../article-item/article-item.component";
import {Filter} from "../../../shared/models/filter.model";
import {FilterComponent} from "../filter/filter.component";
import {FormGroup, FormsModule} from "@angular/forms";
import {Subscription, switchMap} from "rxjs";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {ReviewArtilceCommentItemComponent} from "../review-artilce-comment-item/review-artilce-comment-item.component";
import {CommentListComponent} from "../comment-list/comment-list.component";
import {Comment} from "../../../shared/models/Comment.model";
import {CookieServicing} from "../../../shared/services/cookie.service";
import {CommentService} from "../../../shared/services/comment.service";

@Component({
  selector: 'app-article-list',
  imports: [
    FilterComponent,
    ReviewArtilceCommentItemComponent,
    CommentListComponent,
    FormsModule

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

