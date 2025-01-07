export class User{
  id:string;
  name:string;
  password:string;
  role:string;

  constructor( nickname: string, pass: string , id:string, role: string) {
    this.id = id;
    this.password = pass;
    this.name = nickname;
    this.role = role;
  }
}
