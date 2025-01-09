import {Component, Input} from '@angular/core';
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-review-artilce-comment-item',
  imports: [
    NgClass
  ],
  templateUrl: './review-artilce-comment-item.component.html',
  standalone: true,
  styleUrl: './review-artilce-comment-item.component.css'
})
export class ReviewArtilceCommentItemComponent {
  @Input() article!: ArticleResponse;
}

