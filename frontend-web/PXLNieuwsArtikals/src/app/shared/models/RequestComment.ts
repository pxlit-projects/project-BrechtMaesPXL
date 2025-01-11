export class RequestComment {
  editorsId: string;
  postId: number;
  content: string;

  constructor(content: string, editorsId: string, articleId: number, ) {
    this.content = content;
    this.editorsId = editorsId;
    this.postId = articleId;
  }
}
