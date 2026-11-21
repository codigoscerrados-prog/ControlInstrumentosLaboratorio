import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ITipoInstrumento, NewTipoInstrumento } from '../tipo-instrumento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoInstrumento for edit and NewTipoInstrumentoFormGroupInput for create.
 */
type TipoInstrumentoFormGroupInput = ITipoInstrumento | PartialWithRequiredKeyOf<NewTipoInstrumento>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITipoInstrumento | NewTipoInstrumento> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type TipoInstrumentoFormRawValue = FormValueOf<ITipoInstrumento>;

type NewTipoInstrumentoFormRawValue = FormValueOf<NewTipoInstrumento>;

type TipoInstrumentoFormDefaults = Pick<NewTipoInstrumento, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type TipoInstrumentoFormGroupContent = {
  id: FormControl<TipoInstrumentoFormRawValue['id'] | NewTipoInstrumento['id']>;
  nombre: FormControl<TipoInstrumentoFormRawValue['nombre']>;
  descripcion: FormControl<TipoInstrumentoFormRawValue['descripcion']>;
  fechaCreacion: FormControl<TipoInstrumentoFormRawValue['fechaCreacion']>;
  usuarioCreacion: FormControl<TipoInstrumentoFormRawValue['usuarioCreacion']>;
  fechaModificacion: FormControl<TipoInstrumentoFormRawValue['fechaModificacion']>;
  usuarioModificacion: FormControl<TipoInstrumentoFormRawValue['usuarioModificacion']>;
};

export type TipoInstrumentoFormGroup = FormGroup<TipoInstrumentoFormGroupContent>;

@Service()
export class TipoInstrumentoFormService {
  createTipoInstrumentoFormGroup(tipoInstrumento?: TipoInstrumentoFormGroupInput): TipoInstrumentoFormGroup {
    const tipoInstrumentoRawValue = this.convertTipoInstrumentoToTipoInstrumentoRawValue({
      ...this.getFormDefaults(),
      ...(tipoInstrumento ?? { id: null }),
    });

    return new FormGroup<TipoInstrumentoFormGroupContent>({
      id: new FormControl(
        { value: tipoInstrumentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(tipoInstrumentoRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      descripcion: new FormControl(tipoInstrumentoRawValue.descripcion, {
        validators: [Validators.maxLength(250)],
      }),
      fechaCreacion: new FormControl(tipoInstrumentoRawValue.fechaCreacion),
      usuarioCreacion: new FormControl(tipoInstrumentoRawValue.usuarioCreacion, {
        validators: [Validators.maxLength(50)],
      }),
      fechaModificacion: new FormControl(tipoInstrumentoRawValue.fechaModificacion),
      usuarioModificacion: new FormControl(tipoInstrumentoRawValue.usuarioModificacion, {
        validators: [Validators.maxLength(50)],
      }),
    });
  }

  getTipoInstrumento(form: TipoInstrumentoFormGroup): ITipoInstrumento | NewTipoInstrumento {
    return this.convertTipoInstrumentoRawValueToTipoInstrumento(form.getRawValue());
  }

  resetForm(form: TipoInstrumentoFormGroup, tipoInstrumento: TipoInstrumentoFormGroupInput): void {
    const tipoInstrumentoRawValue = this.convertTipoInstrumentoToTipoInstrumentoRawValue({ ...this.getFormDefaults(), ...tipoInstrumento });
    form.reset({
      ...tipoInstrumentoRawValue,
      id: { value: tipoInstrumentoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TipoInstrumentoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertTipoInstrumentoRawValueToTipoInstrumento(
    rawTipoInstrumento: TipoInstrumentoFormRawValue | NewTipoInstrumentoFormRawValue,
  ): ITipoInstrumento | NewTipoInstrumento {
    return {
      ...rawTipoInstrumento,
      fechaCreacion: dayjs(rawTipoInstrumento.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawTipoInstrumento.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertTipoInstrumentoToTipoInstrumentoRawValue(
    tipoInstrumento: ITipoInstrumento | (Partial<NewTipoInstrumento> & TipoInstrumentoFormDefaults),
  ): TipoInstrumentoFormRawValue | PartialWithRequiredKeyOf<NewTipoInstrumentoFormRawValue> {
    return {
      ...tipoInstrumento,
      fechaCreacion: tipoInstrumento.fechaCreacion ? tipoInstrumento.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: tipoInstrumento.fechaModificacion ? tipoInstrumento.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
