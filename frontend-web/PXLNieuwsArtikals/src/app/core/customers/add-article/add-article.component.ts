import {Component, EventEmitter, inject, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ArticleService} from "../../../shared/services/article.service";
import {CookieServicing} from "../../../shared/services/cookie.service";
import {Article} from "../../../shared/models/Article.model";
import {ArticleTest} from "../../../shared/models/ArticleTest.model";

@Component({
  selector: 'app-add-article',
  imports: [ReactiveFormsModule],
  templateUrl: './add-article.component.html',
  standalone: true,
  styleUrl: './add-article.component.css'
})

export class AddArticleComponent {

  articleService: ArticleService = inject(ArticleService);
  router: Router = inject(Router);

  successMessage: string | null = null;
  errorMessage: string | null = null;

  fb: FormBuilder = inject(FormBuilder);
  articleForm: FormGroup = this.fb.group({
    title: ['', Validators.required],
    content: ['', Validators.required],
    status: ['', Validators.required],
  });

  onSubmit() {
    this.successMessage = null;
    this.errorMessage = null;
    const newArticle: ArticleTest = {
      ...this.articleForm.value,
      editorsId: CookieServicing.getCookie()?.id

    };
    const role = CookieServicing.getCookie()?.role;
    console.log("role", role);
     if (!role) {
      this.router.navigate(['/login']);
      return;
    }

    this.articleService.addArticle(newArticle, role).subscribe({
      next: (response) => {
        this.articleForm.reset();
        console.log("article added successfully.");
        this.successMessage = "article added successfully.";
      },
      error: (err) => {
        this.errorMessage = err.errorMessage;
        console.log("article added " + err.errorMessage) ;

      }
    });
  }

}
