import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'user-management',
    title: 'userManagement.home.title',
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'authority',
    title: 'ejemplo01App.adminAuthority.home.title',
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'rol',
    title: 'ejemplo01App.rol.home.title',
    loadChildren: () => import('./rol/rol.routes'),
  },
  {
    path: 'usuario',
    title: 'ejemplo01App.usuario.home.title',
    loadChildren: () => import('./usuario/usuario.routes'),
  },
  {
    path: 'recuperacion-password',
    title: 'ejemplo01App.recuperacionPassword.home.title',
    loadChildren: () => import('./recuperacion-password/recuperacion-password.routes'),
  },
  {
    path: 'grupo-instrumental',
    title: 'ejemplo01App.grupoInstrumental.home.title',
    loadChildren: () => import('./grupo-instrumental/grupo-instrumental.routes'),
  },
  {
    path: 'tipo-instrumento',
    title: 'ejemplo01App.tipoInstrumento.home.title',
    loadChildren: () => import('./tipo-instrumento/tipo-instrumento.routes'),
  },
  {
    path: 'laboratorio-calibracion',
    title: 'ejemplo01App.laboratorioCalibracion.home.title',
    loadChildren: () => import('./laboratorio-calibracion/laboratorio-calibracion.routes'),
  },
  {
    path: 'activo',
    title: 'ejemplo01App.activo.home.title',
    loadChildren: () => import('./activo/activo.routes'),
  },
  {
    path: 'calibracion',
    title: 'ejemplo01App.calibracion.home.title',
    loadChildren: () => import('./calibracion/calibracion.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;
