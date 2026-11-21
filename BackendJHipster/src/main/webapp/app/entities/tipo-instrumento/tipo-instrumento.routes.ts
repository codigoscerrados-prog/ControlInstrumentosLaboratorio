import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import TipoInstrumentoResolve from './route/tipo-instrumento-routing-resolve.service';

const tipoInstrumentoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tipo-instrumento').then(m => m.TipoInstrumento),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tipo-instrumento-detail').then(m => m.TipoInstrumentoDetail),
    resolve: {
      tipoInstrumento: TipoInstrumentoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tipo-instrumento-update').then(m => m.TipoInstrumentoUpdate),
    resolve: {
      tipoInstrumento: TipoInstrumentoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tipo-instrumento-update').then(m => m.TipoInstrumentoUpdate),
    resolve: {
      tipoInstrumento: TipoInstrumentoResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default tipoInstrumentoRoute;
