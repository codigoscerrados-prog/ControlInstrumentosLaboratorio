import dayjs from 'dayjs/esm';

import { IActivo } from 'app/entities/activo/activo.model';
import { ILaboratorioCalibracion } from 'app/entities/laboratorio-calibracion/laboratorio-calibracion.model';

export interface ICalibracion {
  id: number;
  fechaEnvio?: dayjs.Dayjs | null;
  fechaCalibracion?: dayjs.Dayjs | null;
  fechaRetorno?: dayjs.Dayjs | null;
  fechaVencimiento?: dayjs.Dayjs | null;
  numeroCertificado?: string | null;
  resultado?: string | null;
  observaciones?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioCreacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
  activo?: IActivo | null;
  laboratorio?: ILaboratorioCalibracion | null;
}

export type NewCalibracion = Omit<ICalibracion, 'id'> & { id: null };
