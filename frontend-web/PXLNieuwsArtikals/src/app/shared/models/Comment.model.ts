export class Comment {
  id: number;
  content: string;
  editorsId: string;
  articleId: number;

  constructor(content: string, editorsId: string, articleId: number, id?: number) {
    this.id = id || 0;
    this.content = content;
    this.editorsId = editorsId;
    this.articleId = articleId;
  }
}
