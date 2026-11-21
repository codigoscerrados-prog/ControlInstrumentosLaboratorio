import dayjs from 'dayjs/esm';

export interface ILaboratorioCalibracion {
  id: number;
  nombre?: string | null;
  ruc?: string | null;
  direccion?: string | null;
  telefono?: string | null;
  correo?: string | null;
  estado?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuarioCreacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
  usuarioModificacion?: string | null;
}

export type NewLaboratorioCalibracion = Omit<ILaboratorioCalibracion, 'id'> & { id: null };
