import dayjs from 'dayjs/esm';

import { ICursos, NewCursos } from './cursos.model';

export const sampleWithRequiredData: ICursos = {
  id: 17290,
};

export const sampleWithPartialData: ICursos = {
  id: 30600,
  curso: 'consequently',
  usuarioCreacion: 'courageously abnormally',
  fechaModificacion: dayjs('2026-09-28T19:46'),
};

export const sampleWithFullData: ICursos = {
  id: 7043,
  curso: 'ripe sheepishly',
  usuarioCreacion: 'sadly zowie unlike',
  fechaCreacion: dayjs('2026-09-29T00:17'),
  usuairoModificacion: 'ah reprimand finally',
  fechaModificacion: dayjs('2026-09-28T06:39'),
};

export const sampleWithNewData: NewCursos = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
