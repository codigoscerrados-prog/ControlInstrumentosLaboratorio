import dayjs from 'dayjs/esm';

import { IGrupoInstrumental, NewGrupoInstrumental } from './grupo-instrumental.model';

export const sampleWithRequiredData: IGrupoInstrumental = {
  id: 16499,
  codigo: 'indeed capitalize',
  nombre: 'birdbath victoriously',
};

export const sampleWithPartialData: IGrupoInstrumental = {
  id: 13413,
  codigo: 'hmph supposing CD',
  nombre: 'nearly forswear successfully',
  descripcion: 'blah trusty',
  usuarioModificacion: 'jovially lovingly gah',
};

export const sampleWithFullData: IGrupoInstrumental = {
  id: 19249,
  codigo: 'once except athwart',
  nombre: 'mystify upon',
  descripcion: 'toward beside given',
  fechaCreacion: dayjs('2026-10-27T09:55'),
  usuarioCreacion: 'yieldingly afore hydrolyse',
  fechaModificacion: dayjs('2026-10-27T23:36'),
  usuarioModificacion: 'meanwhile leading lest',
};

export const sampleWithNewData: NewGrupoInstrumental = {
  codigo: 'gosh favorable',
  nombre: 'provided plus as',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
