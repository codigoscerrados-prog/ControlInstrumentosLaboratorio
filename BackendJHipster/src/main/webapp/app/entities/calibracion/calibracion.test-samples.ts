import dayjs from 'dayjs/esm';

import { ICalibracion, NewCalibracion } from './calibracion.model';

export const sampleWithRequiredData: ICalibracion = {
  id: 9442,
};

export const sampleWithPartialData: ICalibracion = {
  id: 10512,
  fechaCalibracion: dayjs('2026-10-27'),
  fechaRetorno: dayjs('2026-10-27'),
  fechaCreacion: dayjs('2026-10-27T20:05'),
  usuarioCreacion: 'anenst',
  usuarioModificacion: 'galvanize',
};

export const sampleWithFullData: ICalibracion = {
  id: 23515,
  fechaEnvio: dayjs('2026-10-27'),
  fechaCalibracion: dayjs('2026-10-27'),
  fechaRetorno: dayjs('2026-10-27'),
  fechaVencimiento: dayjs('2026-10-27'),
  numeroCertificado: 'ugh shark',
  resultado: 'blah destock replicate',
  observaciones: 'ugly hastily',
  fechaCreacion: dayjs('2026-10-27T16:03'),
  usuarioCreacion: 'alliance',
  fechaModificacion: dayjs('2026-10-27T22:01'),
  usuarioModificacion: 'tentacle',
};

export const sampleWithNewData: NewCalibracion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
