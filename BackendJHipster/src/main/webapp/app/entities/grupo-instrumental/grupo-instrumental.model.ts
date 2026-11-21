import dayjs from 'dayjs/esm';

export interface IGrupoInstrumental {
  id: number;
  codigo?: string | null;
  nombre?: string | null;
  descripcion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioCreacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
}

export type NewGrupoInstrumental = Omit<IGrupoInstrumental, 'id'> & { id: null };
