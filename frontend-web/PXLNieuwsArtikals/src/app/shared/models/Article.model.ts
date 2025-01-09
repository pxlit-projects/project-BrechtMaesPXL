export class Article {

   title: string;
   content: string;
   statusArticle: string;
   editorsId:string;
   createdAt?: Date;

  constructor(title: string, content: string, status: string, editorsId: string, createdAt?: Date) {
    this.title = title;
    this.content = content;
    this.statusArticle = status;
    this.editorsId = editorsId;
    this.createdAt = createdAt;
  }
}
