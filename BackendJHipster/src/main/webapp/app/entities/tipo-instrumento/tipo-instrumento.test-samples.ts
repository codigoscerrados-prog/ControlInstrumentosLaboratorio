import dayjs from 'dayjs/esm';

import { ITipoInstrumento, NewTipoInstrumento } from './tipo-instrumento.model';

export const sampleWithRequiredData: ITipoInstrumento = {
  id: 9466,
  nombre: 'bah',
};

export const sampleWithPartialData: ITipoInstrumento = {
  id: 3442,
  nombre: 'boo yowza interestingly',
  fechaModificacion: dayjs('2026-10-27T05:31'),
  usuarioModificacion: 'serpentine',
};

export const sampleWithFullData: ITipoInstrumento = {
  id: 13207,
  nombre: 'abaft lest',
  descripcion: 'consequently although border',
  fechaCreacion: dayjs('2026-10-27T23:24'),
  usuarioCreacion: 'lest whoa sit',
  fechaModificacion: dayjs('2026-10-27T08:53'),
  usuarioModificacion: 'wonderfully naturally',
};

export const sampleWithNewData: NewTipoInstrumento = {
  nombre: 'likewise white',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
