import {Component, Input} from '@angular/core';
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {NgClass} from "@angular/common";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-preview-item',
  imports: [
    NgClass,
    RouterLink
  ],
  templateUrl: './preview-item.component.html',
  standalone: true,
  styleUrl: './preview-item.component.css'
})
export class PreviewItemComponent {
  @Input() article!: ArticleResponse;


}
