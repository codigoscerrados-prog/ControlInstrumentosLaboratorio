import dayjs from 'dayjs/esm';

import { IUsuario, NewUsuario } from './usuario.model';

export const sampleWithRequiredData: IUsuario = {
  id: 23732,
  username: 'though phew',
  password: 'selfishly deliberately dandelion',
  correo: 'lightly',
  estado: 'gadzooks boo',
};

export const sampleWithPartialData: IUsuario = {
  id: 19924,
  username: 'outrun',
  password: 'failing realistic hm',
  correo: 'diligently',
  estado: 'pish',
  usuarioCreacion: 'inquisitively however unlike',
  usuarioModificacion: 'whenever rule uh-huh',
};

export const sampleWithFullData: IUsuario = {
  id: 32608,
  username: 'outlaw lest',
  password: 'nocturnal yum up',
  correo: 'outlandish',
  estado: 'successfully',
  fechaCreacion: dayjs('2026-10-27T03:01'),
  usuarioCreacion: 'impostor whose able',
  fechaModificacion: dayjs('2026-10-28T01:41'),
  usuarioModificacion: 'scenario hurtful per',
};

export const sampleWithNewData: NewUsuario = {
  username: 'beside pish strait',
  password: 'programme electrify',
  correo: 'amongst',
  estado: 'duh settle furthermo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
