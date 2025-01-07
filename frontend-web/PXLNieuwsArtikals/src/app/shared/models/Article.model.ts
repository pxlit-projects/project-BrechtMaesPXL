export class Article {

   title: string;
   content: string;
   statusArticle: string;
   editorsId:string;

  constructor(title: string, content: string, status: string, editorsId: string) {
    this.title = title;
    this.content = content;
    this.statusArticle = status;
    this.editorsId = editorsId;
  }
}
