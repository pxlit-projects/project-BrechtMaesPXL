import { Component, inject, Input, OnInit } from '@angular/core';
import { ArticleService } from "../../../shared/services/article.service";
import { User } from "../../../shared/models/user.model";
import { CookieServicing } from "../../../shared/services/cookie.service";
import { Comment } from "../../../shared/models/Comment.model";
import { FormsModule } from "@angular/forms";
import {RequestComment} from "../../../shared/models/RequestComment";
import {CommentService} from "../../../shared/services/comment.service";

@Component({
  selector: 'app-comment-list',
  imports: [FormsModule],
  templateUrl: './comment-list.component.html',
  standalone: true,
  styleUrl: './comment-list.component.css',
})
export class CommentListComponent implements OnInit {
  @Input() articleId!: string;
  newComments: { [articleId: string]: string } = {};
  filteredData: Comment[] = [];
  editingCommentId: number | null = null;
  editCommentContent: string = '';

  commentService: CommentService = inject(CommentService);
  user: User | null = CookieServicing.getCookie();

  ngOnInit(): void {
    this.fetchData();
  }

  fetchData(): void {
    if (!this.user) return;

    this.commentService.getComments(this.articleId).subscribe({
      next: (data) => {
        this.filteredData = data;
      },
      error: (err) => console.error('Error fetching comments:', err),
    });
  }

  addComment(): void {
    if (!this.user) {
      alert('User not logged in');
      return;
    }

    const commentContent = this.newComments[this.articleId];
    if (commentContent) {
      const newComment = new Comment(commentContent, this.user.name, parseInt(this.articleId, 10));

      this.commentService.addComment(newComment).subscribe({
        next: () => {
          this.newComments[this.articleId] = '';
          this.filteredData = [...this.filteredData, newComment];
        },
        error: (err) => console.error('Error adding comment:', err),
      });
    } else {
      alert('Comment cannot be empty');
    }
  }

  startEditing(comment: Comment): void {
    this.editingCommentId = comment.id;
    this.editCommentContent = comment.content;
  }

  saveEdit(comment: Comment): void {
    if (!this.editCommentContent.trim()) {
      alert('Comment cannot be empty');
      return;
    }

    comment.content = this.editCommentContent.trim();
    this.commentService.updateComment(comment).subscribe({
      next: () => {
        this.editingCommentId = null;
        this.filteredData = this.filteredData.map((c) => (c.id === comment.id ? comment : c));
      },
      error: (err) => console.error('Error updating comment:', err),
    });
  }

  deleteComment(commentId: number): void {
    this.commentService.deleteComment(commentId).subscribe({
      next: () => {
        this.filteredData = this.filteredData.filter((comment) => comment.id !== commentId);
      },
      error: (err) => console.error('Error deleting comment:', err),
    });
  }
}
