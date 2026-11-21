import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ActivoResolve from './route/activo-routing-resolve.service';

const activoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/activo').then(m => m.Activo),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/activo-detail').then(m => m.ActivoDetail),
    resolve: {
      activo: ActivoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/activo-update').then(m => m.ActivoUpdate),
    resolve: {
      activo: ActivoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/activo-update').then(m => m.ActivoUpdate),
    resolve: {
      activo: ActivoResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default activoRoute;
