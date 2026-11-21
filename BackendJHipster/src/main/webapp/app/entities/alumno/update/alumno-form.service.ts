import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IAlumno, NewAlumno } from '../alumno.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlumno for edit and NewAlumnoFormGroupInput for create.
 */
type AlumnoFormGroupInput = IAlumno | PartialWithRequiredKeyOf<NewAlumno>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAlumno | NewAlumno> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type AlumnoFormRawValue = FormValueOf<IAlumno>;

type NewAlumnoFormRawValue = FormValueOf<NewAlumno>;

type AlumnoFormDefaults = Pick<NewAlumno, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type AlumnoFormGroupContent = {
  id: FormControl<AlumnoFormRawValue['id'] | NewAlumno['id']>;
  codigoAlumno: FormControl<AlumnoFormRawValue['codigoAlumno']>;
  apellidoP: FormControl<AlumnoFormRawValue['apellidoP']>;
  apellidoM: FormControl<AlumnoFormRawValue['apellidoM']>;
  nombre: FormControl<AlumnoFormRawValue['nombre']>;
  usuarioCreacion: FormControl<AlumnoFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<AlumnoFormRawValue['fechaCreacion']>;
  usuairoModificacion: FormControl<AlumnoFormRawValue['usuairoModificacion']>;
  fechaModificacion: FormControl<AlumnoFormRawValue['fechaModificacion']>;
};

export type AlumnoFormGroup = FormGroup<AlumnoFormGroupContent>;

@Service()
export class AlumnoFormService {
  createAlumnoFormGroup(alumno?: AlumnoFormGroupInput): AlumnoFormGroup {
    const alumnoRawValue = this.convertAlumnoToAlumnoRawValue({
      ...this.getFormDefaults(),
      ...(alumno ?? { id: null }),
    });

    return new FormGroup<AlumnoFormGroupContent>({
      id: new FormControl(
        { value: alumnoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigoAlumno: new FormControl(alumnoRawValue.codigoAlumno),
      apellidoP: new FormControl(alumnoRawValue.apellidoP),
      apellidoM: new FormControl(alumnoRawValue.apellidoM),
      nombre: new FormControl(alumnoRawValue.nombre),
      usuarioCreacion: new FormControl(alumnoRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(alumnoRawValue.fechaCreacion),
      usuairoModificacion: new FormControl(alumnoRawValue.usuairoModificacion),
      fechaModificacion: new FormControl(alumnoRawValue.fechaModificacion),
    });
  }

  getAlumno(form: AlumnoFormGroup): IAlumno | NewAlumno {
    return this.convertAlumnoRawValueToAlumno(form.getRawValue());
  }

  resetForm(form: AlumnoFormGroup, alumno: AlumnoFormGroupInput): void {
    const alumnoRawValue = this.convertAlumnoToAlumnoRawValue({ ...this.getFormDefaults(), ...alumno });
    form.reset({
      ...alumnoRawValue,
      id: { value: alumnoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): AlumnoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertAlumnoRawValueToAlumno(rawAlumno: AlumnoFormRawValue | NewAlumnoFormRawValue): IAlumno | NewAlumno {
    return {
      ...rawAlumno,
      fechaCreacion: dayjs(rawAlumno.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawAlumno.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertAlumnoToAlumnoRawValue(
    alumno: IAlumno | (Partial<NewAlumno> & AlumnoFormDefaults),
  ): AlumnoFormRawValue | PartialWithRequiredKeyOf<NewAlumnoFormRawValue> {
    return {
      ...alumno,
      fechaCreacion: alumno.fechaCreacion ? alumno.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: alumno.fechaModificacion ? alumno.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
