import {Component, inject, Input, OnInit} from '@angular/core';
import {ArticleResponse} from "../../../shared/models/ArticleRsponse.model";
import {ArticleService} from "../../../shared/services/article.service";
import {User} from "../../../shared/models/user.model";
import {CookieServicing} from "../../../shared/services/cookie.service";
import {CommentService} from "../../../shared/services/comment.service";
import {Comment} from "../../../shared/models/Comment.model";
import {FormsModule} from "@angular/forms";
import {comment} from "postcss";

@Component({
  selector: 'app-comment-list',
  imports: [
    FormsModule
  ],
  templateUrl: './comment-list.component.html',
  standalone: true,
  styleUrl: './comment-list.component.css'
})
export class CommentListComponent implements OnInit {
  @Input() articleId!: string;
  newComments: { [articleId: string]: string } = {};
  filteredData: Comment[] = [];
  commentService: CommentService = inject(CommentService);
  user: User | null = CookieServicing.getCookie();

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    if (!this.user) return;

    this.commentService.getComments(this.articleId).subscribe({
      next: data => {
        this.filteredData = data;
      },
      error: err => console.error("Error fetching comments:", err),
    });
  }

  addComment(): void {
    if (!this.user) {
      alert("User not logged in");
      return;
    }

    const commentContent = this.newComments[this.articleId];
    if (commentContent) {
      const newComment = new Comment(commentContent, this.user.name, parseInt(this.articleId, 10));

      this.commentService.addComment(newComment).subscribe({
        next: () => {
          // Clear the input for the current article
          this.newComments[this.articleId] = '';
          // Add the new comment to the displayed list
          this.filteredData = [...this.filteredData, newComment];
        },
        error: err => console.error("Error adding comment:", err),
      });
    } else {
      alert("Comment cannot be empty");
    }
  }
}
