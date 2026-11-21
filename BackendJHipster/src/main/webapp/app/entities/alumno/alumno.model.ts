import dayjs from 'dayjs/esm';

export interface IAlumno {
  id: number;
  codigoAlumno?: string | null;
  apellidoP?: string | null;
  apellidoM?: string | null;
  nombre?: string | null;
  usuarioCreacion?: string | null;
  fechaCreacion?: dayjs.Dayjs | null;
  usuairoModificacion?: string | null;
  fechaModificacion?: dayjs.Dayjs | null;
}

export type NewAlumno = Omit<IAlumno, 'id'> & { id: null };
