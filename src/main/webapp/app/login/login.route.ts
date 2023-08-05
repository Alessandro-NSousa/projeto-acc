import { Route } from '@angular/router';

import { LoginComponent } from './login.component';

export const LOGIN_ROUTE: Route = {
  path: '',
  component: LoginComponent,
  outlet: 'login',
  // data: {
  //pageTitle: 'login.title',
  //},
};
