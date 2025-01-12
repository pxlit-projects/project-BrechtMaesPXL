import { Injectable } from '@angular/core';
import {InMemoryDbService} from "angular-in-memory-web-api";
import {User} from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class InMemoryDataService implements InMemoryDbService {
  createDb() {
    let users: User[] = [
      {id: "1", name:"Jhon", password:"Jhon", role:"EDITOR"},
      {id: "2", name:"Tim", password:"Tim", role:"USER"},
      {id: "3", name:"Bill", password:"Bill", role:"EDITOR"},


    ];

    return {users};
  }
}
