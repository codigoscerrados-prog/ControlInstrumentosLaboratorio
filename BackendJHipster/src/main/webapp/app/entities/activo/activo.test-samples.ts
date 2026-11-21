import dayjs from 'dayjs/esm';

import { IActivo, NewActivo } from './activo.model';

export const sampleWithRequiredData: IActivo = {
  id: 26023,
  codigo: 'eek scheme even',
  nombre: 'but duh round',
  estado: 'roasted',
};

export const sampleWithPartialData: IActivo = {
  id: 30807,
  codigo: 'before switch',
  nombre: 'eek clinch',
  numeroSerie: 'unless deeply sadly',
  rango: 'extroverted aftermath',
  resolucion: 'rich penalise retract',
  ubicacion: 'yet ew',
  estado: 'ferret stable gosh',
  observaciones: 'an save',
  fechaCreacion: dayjs('2026-10-27T20:35'),
};

export const sampleWithFullData: IActivo = {
  id: 7023,
  codigo: 'yearly resource meaningfully',
  nombre: 'past what',
  marca: 'but gee around',
  modelo: 'frilly far',
  numeroSerie: 'excitable skean replicate',
  rango: 'where',
  resolucion: 'from distorted',
  ubicacion: 'viciously yum upliftingly',
  estado: 'beneath meanwhile mask',
  observaciones: 'safely whoa graceful',
  fechaCreacion: dayjs('2026-10-27T18:05'),
  usuarioCreacion: 'vastly oh',
  fechaModificacion: dayjs('2026-10-27T06:20'),
  usuarioModificacion: 'and',
};

export const sampleWithNewData: NewActivo = {
  codigo: 'and although unexpectedly',
  nombre: 'orderly obvious',
  estado: 'astride quicker',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
