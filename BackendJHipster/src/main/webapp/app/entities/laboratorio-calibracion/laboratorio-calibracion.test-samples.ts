import dayjs from 'dayjs/esm';

import { ILaboratorioCalibracion, NewLaboratorioCalibracion } from './laboratorio-calibracion.model';

export const sampleWithRequiredData: ILaboratorioCalibracion = {
  id: 24382,
  nombre: 'complicated shy beneath',
  estado: 'premier',
};

export const sampleWithPartialData: ILaboratorioCalibracion = {
  id: 27134,
  nombre: 'gadzooks',
  direccion: 'smoothly meh',
  telefono: 'royal',
  estado: 'brightly scamper ath',
  usuarioCreacion: 'lest about',
  fechaModificacion: dayjs('2026-10-27T22:51'),
  usuarioModificacion: 'without drug',
};

export const sampleWithFullData: ILaboratorioCalibracion = {
  id: 14305,
  nombre: 'deceivingly very',
  ruc: 'above',
  direccion: 'laughter',
  telefono: 'yum',
  correo: 'finally',
  estado: 'even outsource nor',
  fechaCreacion: dayjs('2026-10-27T09:52'),
  usuarioCreacion: 'retool',
  fechaModificacion: dayjs('2026-10-27T18:15'),
  usuarioModificacion: 'crazy horde unto',
};

export const sampleWithNewData: NewLaboratorioCalibracion = {
  nombre: 'outrank',
  estado: 'rust accredit hmph',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
