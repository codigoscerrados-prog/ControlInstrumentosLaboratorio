import dayjs from 'dayjs/esm';

import { IRecuperacionPassword, NewRecuperacionPassword } from './recuperacion-password.model';

export const sampleWithRequiredData: IRecuperacionPassword = {
  id: 582,
  token: 'who given',
  fechaSolicitud: dayjs('2026-10-27T21:03'),
  fechaExpiracion: dayjs('2026-10-27T04:02'),
  usado: true,
};

export const sampleWithPartialData: IRecuperacionPassword = {
  id: 23337,
  token: 'wallaby ugh',
  fechaSolicitud: dayjs('2026-10-27T09:44'),
  fechaExpiracion: dayjs('2026-10-28T00:06'),
  usado: true,
};

export const sampleWithFullData: IRecuperacionPassword = {
  id: 5628,
  token: 'hovercraft garrote abaft',
  fechaSolicitud: dayjs('2026-10-27T20:47'),
  fechaExpiracion: dayjs('2026-10-27T05:52'),
  usado: false,
};

export const sampleWithNewData: NewRecuperacionPassword = {
  token: 'vastly doubter',
  fechaSolicitud: dayjs('2026-10-27T23:32'),
  fechaExpiracion: dayjs('2026-10-27T11:11'),
  usado: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
