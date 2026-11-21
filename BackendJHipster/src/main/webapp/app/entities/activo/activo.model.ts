import dayjs from 'dayjs/esm';

import { IGrupoInstrumental } from 'app/entities/grupo-instrumental/grupo-instrumental.model';
import { ITipoInstrumento } from 'app/entities/tipo-instrumento/tipo-instrumento.model';

export interface IActivo {
  id: number;
  codigo?: string | null;
  nombre?: string | null;
  marca?: string | null;
  modelo?: string | null;
  numeroSerie?: string | null;
  rango?: string | null;
  resolucion?: string | null;
  ubicacion?: string | null;
  estado?: string | null;
  observaciones?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioCreacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  grupo?: IGrupoInstrumental | null;
  tipoInstrumento?: ITipoInstrumento | null;
}

export type NewActivo = Omit<IActivo, 'id'> & { id: null };
