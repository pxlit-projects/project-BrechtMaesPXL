import {Component, inject, Input} from '@angular/core';
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {NgClass} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {ReveiwService} from "../../../shared/services/reveiw.service";
import {ArticleService} from "../../../shared/services/article.service";
import { ReviewRequestModel} from "../../../shared/models/ReviewRequest.model";
import {CookieServicing} from "../../../shared/services/cookie.service";

@Component({
  selector: 'app-preview-item',
  imports: [
    NgClass,
    FormsModule
  ],
  templateUrl: './preview-item.component.html',
  standalone: true,
  styleUrl: './preview-item.component.css'
})
export class PreviewItemComponent {
  @Input() article!: ArticleResponse;
  reviewService: ReveiwService = inject(ReveiwService);

  router: Router = inject(Router);

  formData = {
    title: '',
    content: '',
    status: '',
  };

  setStatus(status: string): void {
    this.formData.status = status;
  }

  onSubmit(): void {
    if (this.formData.title && this.formData.content && this.formData.status) {
      const user = CookieServicing.getCookie();
      const name = user?.name;

      if (name) {
        const reviewRequest = new ReviewRequestModel(
          this.formData.title,
          name,
          this.formData.content,
          this.formData.status,
          parseInt(this.article.id, 10)
        );

        this.reviewService.addReview(reviewRequest).subscribe({
          next: () => {
            alert('Review submitted successfully!');
          },
          error: (err) => {
            alert('Failed to submit review. Please try again later.');
          }
        });
      } else {
        this.router.navigate(['/login']);
      }
    } else {
      alert('Please complete the form and select a status before submitting.');
    }
  }
}
