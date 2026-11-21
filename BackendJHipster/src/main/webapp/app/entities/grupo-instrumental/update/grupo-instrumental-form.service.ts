import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IGrupoInstrumental, NewGrupoInstrumental } from '../grupo-instrumental.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGrupoInstrumental for edit and NewGrupoInstrumentalFormGroupInput for create.
 */
type GrupoInstrumentalFormGroupInput = IGrupoInstrumental | PartialWithRequiredKeyOf<NewGrupoInstrumental>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IGrupoInstrumental | NewGrupoInstrumental> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type GrupoInstrumentalFormRawValue = FormValueOf<IGrupoInstrumental>;

type NewGrupoInstrumentalFormRawValue = FormValueOf<NewGrupoInstrumental>;

type GrupoInstrumentalFormDefaults = Pick<NewGrupoInstrumental, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type GrupoInstrumentalFormGroupContent = {
  id: FormControl<GrupoInstrumentalFormRawValue['id'] | NewGrupoInstrumental['id']>;
  codigo: FormControl<GrupoInstrumentalFormRawValue['codigo']>;
  nombre: FormControl<GrupoInstrumentalFormRawValue['nombre']>;
  descripcion: FormControl<GrupoInstrumentalFormRawValue['descripcion']>;
  fechaCreacion: FormControl<GrupoInstrumentalFormRawValue['fechaCreacion']>;
  usuarioCreacion: FormControl<GrupoInstrumentalFormRawValue['usuarioCreacion']>;
  fechaModificacion: FormControl<GrupoInstrumentalFormRawValue['fechaModificacion']>;
  usuarioModificacion: FormControl<GrupoInstrumentalFormRawValue['usuarioModificacion']>;
};

export type GrupoInstrumentalFormGroup = FormGroup<GrupoInstrumentalFormGroupContent>;

@Service()
export class GrupoInstrumentalFormService {
  createGrupoInstrumentalFormGroup(grupoInstrumental?: GrupoInstrumentalFormGroupInput): GrupoInstrumentalFormGroup {
    const grupoInstrumentalRawValue = this.convertGrupoInstrumentalToGrupoInstrumentalRawValue({
      ...this.getFormDefaults(),
      ...(grupoInstrumental ?? { id: null }),
    });

    return new FormGroup<GrupoInstrumentalFormGroupContent>({
      id: new FormControl(
        { value: grupoInstrumentalRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(grupoInstrumentalRawValue.codigo, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      nombre: new FormControl(grupoInstrumentalRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      descripcion: new FormControl(grupoInstrumentalRawValue.descripcion, {
        validators: [Validators.maxLength(250)],
      }),
      fechaCreacion: new FormControl(grupoInstrumentalRawValue.fechaCreacion),
      usuarioCreacion: new FormControl(grupoInstrumentalRawValue.usuarioCreacion, {
        validators: [Validators.maxLength(50)],
      }),
      fechaModificacion: new FormControl(grupoInstrumentalRawValue.fechaModificacion),
      usuarioModificacion: new FormControl(grupoInstrumentalRawValue.usuarioModificacion, {
        validators: [Validators.maxLength(50)],
      }),
    });
  }

  getGrupoInstrumental(form: GrupoInstrumentalFormGroup): IGrupoInstrumental | NewGrupoInstrumental {
    return this.convertGrupoInstrumentalRawValueToGrupoInstrumental(form.getRawValue());
  }

  resetForm(form: GrupoInstrumentalFormGroup, grupoInstrumental: GrupoInstrumentalFormGroupInput): void {
    const grupoInstrumentalRawValue = this.convertGrupoInstrumentalToGrupoInstrumentalRawValue({
      ...this.getFormDefaults(),
      ...grupoInstrumental,
    });
    form.reset({
      ...grupoInstrumentalRawValue,
      id: { value: grupoInstrumentalRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): GrupoInstrumentalFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertGrupoInstrumentalRawValueToGrupoInstrumental(
    rawGrupoInstrumental: GrupoInstrumentalFormRawValue | NewGrupoInstrumentalFormRawValue,
  ): IGrupoInstrumental | NewGrupoInstrumental {
    return {
      ...rawGrupoInstrumental,
      fechaCreacion: dayjs(rawGrupoInstrumental.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawGrupoInstrumental.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertGrupoInstrumentalToGrupoInstrumentalRawValue(
    grupoInstrumental: IGrupoInstrumental | (Partial<NewGrupoInstrumental> & GrupoInstrumentalFormDefaults),
  ): GrupoInstrumentalFormRawValue | PartialWithRequiredKeyOf<NewGrupoInstrumentalFormRawValue> {
    return {
      ...grupoInstrumental,
      fechaCreacion: grupoInstrumental.fechaCreacion ? grupoInstrumental.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: grupoInstrumental.fechaModificacion ? grupoInstrumental.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
