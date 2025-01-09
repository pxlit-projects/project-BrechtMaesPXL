import {Component, inject, OnInit} from '@angular/core';
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {ArticleService} from "../../../shared/services/article.service";
import {ArticleItemComponent} from "../article-item/article-item.component";
import {CookieServicing} from "../../../shared/services/cookie.service";
import {UserService} from "../../../shared/services/user.service";
import {User} from "../../../shared/models/user.model";
import {forkJoin} from "rxjs";

@Component({
  selector: 'app-draft-artilce-list',
  imports: [
    ArticleItemComponent
  ],
  templateUrl: './draft-artilce-list.component.html',
  standalone: true,
  styleUrl: './draft-artilce-list.component.css'
})
export class DraftArtilceListComponent implements OnInit{
  filteredData!: ArticleResponse[];
  articleService: ArticleService = inject(ArticleService);
  user: User | null = CookieServicing.getCookie();

  ngOnInit(): void {
    this.fetchData()

  }


  fetchData(): void {
    if (!this.user) return;

    const id: string = this.user.id;

    forkJoin([
      this.articleService.getArticlesOfEditorByStatus("DRAFT", id),
      this.articleService.getArticlesOfEditorByStatus("REVIEW", id)
    ]).subscribe({
      next: ([draftData, reviewData]) => {
        this.filteredData = [...draftData, ...reviewData];
      },
      error: err => {
        console.error("Error fetching articles:", err);
      }
    });
  }
}
