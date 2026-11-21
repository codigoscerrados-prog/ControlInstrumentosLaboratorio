import dayjs from 'dayjs/esm';

import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IRecuperacionPassword {
  id: number;
  token?: string | null;
  fechaSolicitud?: dayjs.Dayjs | null;
  fechaExpiracion?: dayjs.Dayjs | null;
  usado?: boolean | null;
  usuario?: IUsuario | null;
}

export type NewRecuperacionPassword = Omit<IRecuperacionPassword, 'id'> & { id: null };
