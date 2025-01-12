import { Routes } from '@angular/router';
import {ArticleListComponent} from "./core/customers/article-list/article-list.component";
import {AddArticleComponent} from "./core/customers/add-article/add-article.component";
import {ArticleDetailComponent} from "./core/customers/article-detail/article-detail.component";
import {confirmLeaveGuard} from "./confirm-leave.guard";
import {UesrLoginComponent} from "./core/login/uesr-login/uesr-login.component";
import {RoleGuard} from "./shared/Guard/role.guard";
import {DraftArtilceListComponent} from "./core/customers/draft-artilce-list/draft-artilce-list.component";
import {PreviewListComponent} from "./core/preview/preview-list/preview-list.component";

export const routes: Routes = [
  { path: 'customers', component: ArticleListComponent },
  { path: '', redirectTo: 'customers', pathMatch: 'full' },
  { path: 'customer/:id', component: ArticleDetailComponent,
    canActivate: [RoleGuard],
    data: { role: 'EDITOR' },
  },
  {
    path: 'add',
    component: AddArticleComponent,
    canDeactivate: [confirmLeaveGuard],
    canActivate: [RoleGuard],
    data: { role: 'EDITOR' },
  },
  { path: 'login', component: UesrLoginComponent },
  {
    path: 'draft',
    component: DraftArtilceListComponent,
    canActivate: [RoleGuard],
    data: { role: 'EDITOR' },
  },
  {
    path: 'review',
    component: PreviewListComponent,
    canActivate: [RoleGuard],
    data: { role: 'EDITOR' },
  }
];
