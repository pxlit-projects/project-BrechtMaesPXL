import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Customer } from './shared/models/customer.model';

import {NavbarComponent} from "./core/navbar/navbar.component";
import {ArticleListComponent} from "./core/customers/article-list/article-list.component";
import {CookieServicing} from "./shared/services/cookie.service";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent{
  constructor(private cookieService: CookieService) {
    CookieServicing.setInstance(this.cookieService)

  }


}
