import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CalibracionResolve from './route/calibracion-routing-resolve.service';

const calibracionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/calibracion').then(m => m.Calibracion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/calibracion-detail').then(m => m.CalibracionDetail),
    resolve: {
      calibracion: CalibracionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/calibracion-update').then(m => m.CalibracionUpdate),
    resolve: {
      calibracion: CalibracionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/calibracion-update').then(m => m.CalibracionUpdate),
    resolve: {
      calibracion: CalibracionResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default calibracionRoute;
