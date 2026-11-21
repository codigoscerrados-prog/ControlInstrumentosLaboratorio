import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CursosResolve from './route/cursos-routing-resolve.service';

const cursosRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cursos').then(m => m.Cursos),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cursos-detail').then(m => m.CursosDetail),
    resolve: {
      cursos: CursosResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cursos-update').then(m => m.CursosUpdate),
    resolve: {
      cursos: CursosResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cursos-update').then(m => m.CursosUpdate),
    resolve: {
      cursos: CursosResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default cursosRoute;
