export class Comment {
  Id?: string;
  editorsId: string;
  postId: number;
  content: string;


  constructor( content: string, editorsId: string, postId: number, id? :string) {
    this.content = content;
    this.postId = postId;
    this.editorsId = editorsId;
    this.Id = id
  }
}
