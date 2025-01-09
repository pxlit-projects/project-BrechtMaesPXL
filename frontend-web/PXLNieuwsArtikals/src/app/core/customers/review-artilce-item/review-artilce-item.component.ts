import {Component, Input} from '@angular/core';
import {ReviewModel} from "../../../shared/models/review.model";

@Component({
  selector: 'app-review-artilce-item',
  imports: [],
  templateUrl: './review-artilce-item.component.html',
  standalone: true,
  styleUrl: './review-artilce-item.component.css'
})
export class ReviewArtilceItemComponent {
  @Input() review!: ReviewModel;


}
