import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ICursos, NewCursos } from '../cursos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICursos for edit and NewCursosFormGroupInput for create.
 */
type CursosFormGroupInput = ICursos | PartialWithRequiredKeyOf<NewCursos>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICursos | NewCursos> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type CursosFormRawValue = FormValueOf<ICursos>;

type NewCursosFormRawValue = FormValueOf<NewCursos>;

type CursosFormDefaults = Pick<NewCursos, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type CursosFormGroupContent = {
  id: FormControl<CursosFormRawValue['id'] | NewCursos['id']>;
  curso: FormControl<CursosFormRawValue['curso']>;
  usuarioCreacion: FormControl<CursosFormRawValue['usuarioCreacion']>;
  fechaCreacion: FormControl<CursosFormRawValue['fechaCreacion']>;
  usuairoModificacion: FormControl<CursosFormRawValue['usuairoModificacion']>;
  fechaModificacion: FormControl<CursosFormRawValue['fechaModificacion']>;
};

export type CursosFormGroup = FormGroup<CursosFormGroupContent>;

@Service()
export class CursosFormService {
  createCursosFormGroup(cursos?: CursosFormGroupInput): CursosFormGroup {
    const cursosRawValue = this.convertCursosToCursosRawValue({
      ...this.getFormDefaults(),
      ...(cursos ?? { id: null }),
    });

    return new FormGroup<CursosFormGroupContent>({
      id: new FormControl(
        { value: cursosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      curso: new FormControl(cursosRawValue.curso),
      usuarioCreacion: new FormControl(cursosRawValue.usuarioCreacion),
      fechaCreacion: new FormControl(cursosRawValue.fechaCreacion),
      usuairoModificacion: new FormControl(cursosRawValue.usuairoModificacion),
      fechaModificacion: new FormControl(cursosRawValue.fechaModificacion),
    });
  }

  getCursos(form: CursosFormGroup): ICursos | NewCursos {
    return this.convertCursosRawValueToCursos(form.getRawValue());
  }

  resetForm(form: CursosFormGroup, cursos: CursosFormGroupInput): void {
    const cursosRawValue = this.convertCursosToCursosRawValue({ ...this.getFormDefaults(), ...cursos });
    form.reset({
      ...cursosRawValue,
      id: { value: cursosRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CursosFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertCursosRawValueToCursos(rawCursos: CursosFormRawValue | NewCursosFormRawValue): ICursos | NewCursos {
    return {
      ...rawCursos,
      fechaCreacion: dayjs(rawCursos.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawCursos.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertCursosToCursosRawValue(
    cursos: ICursos | (Partial<NewCursos> & CursosFormDefaults),
  ): CursosFormRawValue | PartialWithRequiredKeyOf<NewCursosFormRawValue> {
    return {
      ...cursos,
      fechaCreacion: cursos.fechaCreacion ? cursos.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: cursos.fechaModificacion ? cursos.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
