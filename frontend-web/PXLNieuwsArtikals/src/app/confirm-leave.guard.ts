import { CanDeactivateFn } from '@angular/router';
import { AddArticleComponent } from './core/customers/add-article/add-article.component';

export const confirmLeaveGuard: CanDeactivateFn<AddArticleComponent> = (component, currentRoute, currentState, nextState) => {
  if(component.articleForm.dirty){
    return window.confirm("Are you sure you want to leave this page?");
  }else{
    return true;
  }
};
