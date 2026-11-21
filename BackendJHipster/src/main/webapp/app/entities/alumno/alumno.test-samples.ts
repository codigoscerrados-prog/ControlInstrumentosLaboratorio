import dayjs from 'dayjs/esm';

import { IAlumno, NewAlumno } from './alumno.model';

export const sampleWithRequiredData: IAlumno = {
  id: 8232,
};

export const sampleWithPartialData: IAlumno = {
  id: 18866,
  apellidoP: 'yearn',
  fechaCreacion: dayjs('2026-09-28T03:11'),
};

export const sampleWithFullData: IAlumno = {
  id: 4680,
  codigoAlumno: 'admonish focused',
  apellidoP: 'tenderly beneath',
  apellidoM: 'but once when',
  nombre: 'gosh meh',
  usuarioCreacion: 'hence mysterious into',
  fechaCreacion: dayjs('2026-09-28T18:58'),
  usuairoModificacion: 'venture',
  fechaModificacion: dayjs('2026-09-28T07:13'),
};

export const sampleWithNewData: NewAlumno = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
