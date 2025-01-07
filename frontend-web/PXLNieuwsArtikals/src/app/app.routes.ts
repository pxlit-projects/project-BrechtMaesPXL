import { Routes } from '@angular/router';
import {ArticleListComponent} from "./core/customers/article-list/article-list.component";
import {AddArticleComponent} from "./core/customers/add-article/add-article.component";
import {CustomerDetailComponent} from "./core/customers/article-detail/customer-detail.component";
import {confirmLeaveGuard} from "./confirm-leave.guard";
import {UesrLoginComponent} from "./core/login/uesr-login/uesr-login.component";
import {RoleGuard} from "./shared/Guard/role.guard";

export const routes: Routes = [
  { path: 'customers', component: ArticleListComponent },
  { path: '', redirectTo: 'customers', pathMatch: 'full' },
  { path: 'customer/:id', component: CustomerDetailComponent },
  {
    path: 'add',
    component: AddArticleComponent,
    canDeactivate: [confirmLeaveGuard],
    canActivate: [RoleGuard],
    data: { role: 'EDITOR' },
  },
  { path: 'login', component: UesrLoginComponent },
];
