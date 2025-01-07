import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {User} from "../models/user.model";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  api: string = environment.apiUrl;
  http: HttpClient = inject(HttpClient);

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.api);
  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.api, user);
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.api}/${user.id}`, user);
  }

  deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
  findUserByUsernameAndPassword(username: string, password: string): Observable<User | null> {
    return this.http.get<User[]>(`${this.api}?name=${username}&password=${password}`).pipe(
      map((users) => (users.length > 0 ? users[0] : null)) // Return the first matching user or null
    );
  }
}
