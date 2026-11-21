import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import GrupoInstrumentalResolve from './route/grupo-instrumental-routing-resolve.service';

const grupoInstrumentalRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/grupo-instrumental').then(m => m.GrupoInstrumental),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/grupo-instrumental-detail').then(m => m.GrupoInstrumentalDetail),
    resolve: {
      grupoInstrumental: GrupoInstrumentalResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/grupo-instrumental-update').then(m => m.GrupoInstrumentalUpdate),
    resolve: {
      grupoInstrumental: GrupoInstrumentalResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/grupo-instrumental-update').then(m => m.GrupoInstrumentalUpdate),
    resolve: {
      grupoInstrumental: GrupoInstrumentalResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default grupoInstrumentalRoute;
