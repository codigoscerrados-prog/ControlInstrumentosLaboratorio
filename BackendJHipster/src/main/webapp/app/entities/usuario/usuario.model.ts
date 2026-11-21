import dayjs from 'dayjs/esm';

import { IRol } from 'app/entities/rol/rol.model';

export interface IUsuario {
  id: number;
  username?: string | null;
  password?: string | null;
  correo?: string | null;
  estado?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioCreacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  rol?: IRol | null;
}

export type NewUsuario = Omit<IUsuario, 'id'> & { id: null };
