export class ReviewRequestModel {
  editorsId:string;
  postId: number;
  type:string;
  title:string;
  content:string;

  constructor( title: string, editorsId: string, content: string, type: string , postId: number) {
    this.content = content;
    this.title = title;
    this.type = type;
    this.postId = postId;
    this.editorsId = editorsId;
  }
}
