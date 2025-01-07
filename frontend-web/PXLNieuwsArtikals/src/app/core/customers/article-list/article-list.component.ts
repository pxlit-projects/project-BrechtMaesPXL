import {Component, inject, OnInit} from '@angular/core';
import {User} from "../../../shared/models/user.model";
import {UserService} from "../../../shared/services/user.service";
import {ArticleService} from "../../../shared/services/article.service";
import {Article} from "../../../shared/models/Article.model";
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {ArticleItemComponent} from "../article-item/article-item.component";

@Component({
  selector: 'app-article-list',
  imports: [
    ArticleItemComponent

  ],
  templateUrl: './article-list.component.html',
  standalone: true,
  styleUrl: './article-list.component.css'
})
export class ArticleListComponent implements OnInit {
  filteredData!: ArticleResponse[];
  articleService: ArticleService = inject(ArticleService);

  ngOnInit(): void {
    this.fetchData()
  }


  fetchData(): void {
    this.articleService.getArticlesByStatus("PUBLISHED").subscribe({
      next: data => {
        this.filteredData = data;
      }
    })
  }

  // handleFilter(filter: Filter){
  //   this.customerService.filterCustomers(filter).subscribe({
  //     next: customers => this.filteredData = customers
  //   });
  // }


}
