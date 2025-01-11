import {Component, inject, Input} from '@angular/core';
import {Customer} from "../../../shared/models/customer.model";
import {NgClass} from "@angular/common";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {User} from "../../../shared/models/user.model";
import {Article} from "../../../shared/models/Article.model";
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {ArticleService} from "../../../shared/services/article.service";

@Component({
  selector: 'app-article-item',
  imports: [
    NgClass,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './article-item.component.html',
  standalone: true,
  styleUrl: './article-item.component.css'
})
export class ArticleItemComponent {
  @Input() article!: ArticleResponse;
  articleService: ArticleService = inject(ArticleService);

  onNotificationClick(event: MouseEvent): void {
    event.stopPropagation();
    this.articleService.updateNotification(parseInt(this.article.id)).subscribe(

    );

  }
}
