export class Comment {

  editorsId: string;
  postId: number;
  content: string;


  constructor( content: string, editorsId: string, postId: number) {
    this.content = content;
    this.postId = postId;
    this.editorsId = editorsId;
  }
}
