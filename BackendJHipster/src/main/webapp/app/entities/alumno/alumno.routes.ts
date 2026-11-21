import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import AlumnoResolve from './route/alumno-routing-resolve.service';

const alumnoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/alumno').then(m => m.Alumno),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/alumno-detail').then(m => m.AlumnoDetail),
    resolve: {
      alumno: AlumnoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/alumno-update').then(m => m.AlumnoUpdate),
    resolve: {
      alumno: AlumnoResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/alumno-update').then(m => m.AlumnoUpdate),
    resolve: {
      alumno: AlumnoResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default alumnoRoute;
