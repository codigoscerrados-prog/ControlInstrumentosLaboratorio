import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import RecuperacionPasswordResolve from './route/recuperacion-password-routing-resolve.service';

const recuperacionPasswordRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/recuperacion-password').then(m => m.RecuperacionPassword),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/recuperacion-password-detail').then(m => m.RecuperacionPasswordDetail),
    resolve: {
      recuperacionPassword: RecuperacionPasswordResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/recuperacion-password-update').then(m => m.RecuperacionPasswordUpdate),
    resolve: {
      recuperacionPassword: RecuperacionPasswordResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/recuperacion-password-update').then(m => m.RecuperacionPasswordUpdate),
    resolve: {
      recuperacionPassword: RecuperacionPasswordResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default recuperacionPasswordRoute;
