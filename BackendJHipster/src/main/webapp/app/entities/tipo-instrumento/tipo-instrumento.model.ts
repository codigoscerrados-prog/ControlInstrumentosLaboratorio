import dayjs from 'dayjs/esm';

export interface ITipoInstrumento {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioCreacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
}

export type NewTipoInstrumento = Omit<ITipoInstrumento, 'id'> & { id: null };
