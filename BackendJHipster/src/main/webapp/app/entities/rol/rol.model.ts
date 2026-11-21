import dayjs from 'dayjs/esm';

export interface IRol {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioCreacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
}

export type NewRol = Omit<IRol, 'id'> & { id: null };
