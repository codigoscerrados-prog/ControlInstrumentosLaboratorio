import dayjs from 'dayjs/esm';

import { IRol, NewRol } from './rol.model';

export const sampleWithRequiredData: IRol = {
  id: 27420,
  nombre: 'underneath nautical unrealistic',
};

export const sampleWithPartialData: IRol = {
  id: 4917,
  nombre: 'where decode',
  descripcion: 'until silver inasmuch',
  fechaCreacion: dayjs('2026-10-27T13:03'),
  usuarioCreacion: 'acidly ouch',
};

export const sampleWithFullData: IRol = {
  id: 7094,
  nombre: 'bog',
  descripcion: 'dilate gym brr',
  fechaCreacion: dayjs('2026-10-27T16:01'),
  usuarioCreacion: 'scarily',
  fechaModificacion: dayjs('2026-10-27T18:21'),
  usuarioModificacion: 'phew cleverly',
};

export const sampleWithNewData: NewRol = {
  nombre: 'faithfully outflank nor',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
