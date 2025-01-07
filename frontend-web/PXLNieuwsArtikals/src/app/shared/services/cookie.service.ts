import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { User } from '../models/user.model';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class CookieServicing {

  static instance: CookieService;

  static setInstance(cookieService: CookieService) {
    CookieServicing.instance = cookieService;
  }

  static setCookie(user: User) {
    if (!CookieServicing.instance) {
      throw new Error('CookieService instance is not set.');
    }
    CookieServicing.instance.set('user', JSON.stringify(user));

  }

  static getCookie(): User | null {
    if (!CookieServicing.instance) {
      throw new Error('CookieService instance is not set.');
    }
    const userString = CookieServicing.instance.get('user');
    return userString ? JSON.parse(userString) : null;
  }
  static removeCookie(): void {
    if (!CookieServicing.instance) {
      throw new Error('CookieService instance is not set.');
    }
    CookieServicing.instance.delete('user');

  }

}
