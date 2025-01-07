
export class ArticleResponse {

  id: string;
  title: string;
  content: string;
  statusArticle: string;
  createdAt: string;
  approvedBy: String[];
  editorsId:string;

  constructor(id: string,title: string, content: string, status: string, editorsId: string, createdAt: string, approvedBy: String[]) {
    this.title = title;
    this.content = content;
    this.statusArticle = status;
    this.editorsId = editorsId;
    this.createdAt = createdAt;
    this.approvedBy = approvedBy;
    this.id = id;
  }
}
