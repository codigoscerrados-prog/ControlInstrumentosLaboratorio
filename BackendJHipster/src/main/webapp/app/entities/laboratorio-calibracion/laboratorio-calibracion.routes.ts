import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import LaboratorioCalibracionResolve from './route/laboratorio-calibracion-routing-resolve.service';

const laboratorioCalibracionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/laboratorio-calibracion').then(m => m.LaboratorioCalibracion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/laboratorio-calibracion-detail').then(m => m.LaboratorioCalibracionDetail),
    resolve: {
      laboratorioCalibracion: LaboratorioCalibracionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/laboratorio-calibracion-update').then(m => m.LaboratorioCalibracionUpdate),
    resolve: {
      laboratorioCalibracion: LaboratorioCalibracionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/laboratorio-calibracion-update').then(m => m.LaboratorioCalibracionUpdate),
    resolve: {
      laboratorioCalibracion: LaboratorioCalibracionResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default laboratorioCalibracionRoute;
