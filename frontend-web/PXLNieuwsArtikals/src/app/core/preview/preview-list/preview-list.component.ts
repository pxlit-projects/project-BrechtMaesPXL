import {Component, inject, OnInit} from '@angular/core';
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {ArticleService} from "../../../shared/services/article.service";
import {User} from "../../../shared/models/user.model";
import {CookieServicing} from "../../../shared/services/cookie.service";
import {PreviewItemComponent} from "../preview-item/preview-item.component";

@Component({
  selector: 'app-preview-list',
  imports: [
    PreviewItemComponent
  ],
  templateUrl: './preview-list.component.html',
  standalone: true,
  styleUrl: './preview-list.component.css'
})
export class PreviewListComponent  implements OnInit{
  filteredData!: ArticleResponse[];
  articleService: ArticleService = inject(ArticleService);
  user: User | null = CookieServicing.getCookie();

  ngOnInit(): void {
    this.fetchData()

  }


  fetchData(): void {
    if (!this.user) return;
    this.articleService.getArticlesOfEditorByStatus("REVIEW", this.user.id).subscribe({
      next: data => {
        this.filteredData = data;
      }
    })
  }
}
