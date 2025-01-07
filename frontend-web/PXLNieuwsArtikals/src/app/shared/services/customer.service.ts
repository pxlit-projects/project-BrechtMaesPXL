import {inject, Injectable} from '@angular/core';
import {Customer} from "../models/customer.model";
import {Filter} from "../models/filter.model";
import {map, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {User} from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  api: string = environment.apiUrl;
  http: HttpClient = inject(HttpClient);

  getCustomers(): Observable<User[]> {
    return this.http.get<User[]>(this.api);
  }

  addCustomer(user: User): Observable<User> {
    return this.http.post<User>(this.api, user);
  }

  updateCustomer(customer: Customer): Observable<Customer>{
    return this.http.put<Customer>('/api/customers/' + customer.id, customer);
  }

  getCustomer(id: number): Observable<Customer> {
    return this.http.get<Customer>('api/customers/' + id);
  }

}
