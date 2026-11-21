import dayjs from 'dayjs/esm';

export interface ICursos {
  id: number;
  curso?: string | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuairoModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
}

export type NewCursos = Omit<ICursos, 'id'> & { id: null };
