import { Component } from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";
import {CookieServicing} from "../../shared/services/cookie.service";
import {User} from "../../shared/models/user.model";

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './navbar.component.html',
  standalone: true,
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  user: User | null = CookieServicing.getCookie();

}
