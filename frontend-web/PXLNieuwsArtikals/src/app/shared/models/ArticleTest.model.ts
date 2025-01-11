export class ArticleTest {

  editorsId: string;
  title: string;
  content: string;
  statusArticle:string;

  constructor(title: string, content: string, statusArticle: string, editorsId: string) {
    this.title = title;
    this.content = content;
    this.statusArticle = statusArticle;
    this.editorsId = editorsId;
  }
}
