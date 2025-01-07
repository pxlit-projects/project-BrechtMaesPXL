
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CookieServicing } from '../../shared/services/cookie.service';
import { User } from '../../shared/models/user.model';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | Observable<boolean> | Promise<boolean> {
    const user: User | null = CookieServicing.getCookie();
    const requiredRole = route.data['role'] as string;

    if (user && user.role === requiredRole) {
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }
}
