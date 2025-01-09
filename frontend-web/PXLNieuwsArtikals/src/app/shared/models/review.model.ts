export class ReviewModel {
  id?:string;
  editorsId:string;
  postId:string;
  type:string;
  title:string;
  content:string;

  constructor( title: string, editorsId: string, content: string, type: string , postId: string, id?:string) {
    this.id = id;
    this.content = content;
    this.title = title;
    this.type = type;
    this.postId = postId;
    this.editorsId = editorsId;
  }
}
