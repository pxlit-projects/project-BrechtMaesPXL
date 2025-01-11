
export class ArticleResponse {

  id: string;
  title: string;
  content: string;
  statusArticle: string;
  createdAt: string;
  approvedBy: string[];
  rejectedBy: string[];
  notification: number;
  editorsId:string;

  constructor(
    id: string,
    title: string,
    content: string,
    status: string,
    editorsId: string,
    createdAt: string,
    approvedBy: string[],
    rejectedBy: string[],
    notification: number)
  {
    this.title = title;
    this.content = content;
    this.statusArticle = status;
    this.editorsId = editorsId;
    this.createdAt = createdAt;
    this.approvedBy = approvedBy;
    this.id = id;
    this.rejectedBy = rejectedBy;
    this.notification = notification;
  }
}
