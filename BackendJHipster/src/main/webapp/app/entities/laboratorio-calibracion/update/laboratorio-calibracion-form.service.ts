import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ILaboratorioCalibracion, NewLaboratorioCalibracion } from '../laboratorio-calibracion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILaboratorioCalibracion for edit and NewLaboratorioCalibracionFormGroupInput for create.
 */
type LaboratorioCalibracionFormGroupInput = ILaboratorioCalibracion | PartialWithRequiredKeyOf<NewLaboratorioCalibracion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILaboratorioCalibracion | NewLaboratorioCalibracion> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type LaboratorioCalibracionFormRawValue = FormValueOf<ILaboratorioCalibracion>;

type NewLaboratorioCalibracionFormRawValue = FormValueOf<NewLaboratorioCalibracion>;

type LaboratorioCalibracionFormDefaults = Pick<NewLaboratorioCalibracion, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type LaboratorioCalibracionFormGroupContent = {
  id: FormControl<LaboratorioCalibracionFormRawValue['id'] | NewLaboratorioCalibracion['id']>;
  nombre: FormControl<LaboratorioCalibracionFormRawValue['nombre']>;
  ruc: FormControl<LaboratorioCalibracionFormRawValue['ruc']>;
  direccion: FormControl<LaboratorioCalibracionFormRawValue['direccion']>;
  telefono: FormControl<LaboratorioCalibracionFormRawValue['telefono']>;
  correo: FormControl<LaboratorioCalibracionFormRawValue['correo']>;
  estado: FormControl<LaboratorioCalibracionFormRawValue['estado']>;
  fechaCreacion: FormControl<LaboratorioCalibracionFormRawValue['fechaCreacion']>;
  usuarioCreacion: FormControl<LaboratorioCalibracionFormRawValue['usuarioCreacion']>;
  fechaModificacion: FormControl<LaboratorioCalibracionFormRawValue['fechaModificacion']>;
  usuarioModificacion: FormControl<LaboratorioCalibracionFormRawValue['usuarioModificacion']>;
};

export type LaboratorioCalibracionFormGroup = FormGroup<LaboratorioCalibracionFormGroupContent>;

@Service()
export class LaboratorioCalibracionFormService {
  createLaboratorioCalibracionFormGroup(laboratorioCalibracion?: LaboratorioCalibracionFormGroupInput): LaboratorioCalibracionFormGroup {
    const laboratorioCalibracionRawValue = this.convertLaboratorioCalibracionToLaboratorioCalibracionRawValue({
      ...this.getFormDefaults(),
      ...(laboratorioCalibracion ?? { id: null }),
    });

    return new FormGroup<LaboratorioCalibracionFormGroupContent>({
      id: new FormControl(
        { value: laboratorioCalibracionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(laboratorioCalibracionRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      ruc: new FormControl(laboratorioCalibracionRawValue.ruc, {
        validators: [Validators.maxLength(20)],
      }),
      direccion: new FormControl(laboratorioCalibracionRawValue.direccion, {
        validators: [Validators.maxLength(250)],
      }),
      telefono: new FormControl(laboratorioCalibracionRawValue.telefono, {
        validators: [Validators.maxLength(30)],
      }),
      correo: new FormControl(laboratorioCalibracionRawValue.correo, {
        validators: [Validators.maxLength(100)],
      }),
      estado: new FormControl(laboratorioCalibracionRawValue.estado, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      fechaCreacion: new FormControl(laboratorioCalibracionRawValue.fechaCreacion),
      usuarioCreacion: new FormControl(laboratorioCalibracionRawValue.usuarioCreacion, {
        validators: [Validators.maxLength(50)],
      }),
      fechaModificacion: new FormControl(laboratorioCalibracionRawValue.fechaModificacion),
      usuarioModificacion: new FormControl(laboratorioCalibracionRawValue.usuarioModificacion, {
        validators: [Validators.maxLength(50)],
      }),
    });
  }

  getLaboratorioCalibracion(form: LaboratorioCalibracionFormGroup): ILaboratorioCalibracion | NewLaboratorioCalibracion {
    return this.convertLaboratorioCalibracionRawValueToLaboratorioCalibracion(form.getRawValue());
  }

  resetForm(form: LaboratorioCalibracionFormGroup, laboratorioCalibracion: LaboratorioCalibracionFormGroupInput): void {
    const laboratorioCalibracionRawValue = this.convertLaboratorioCalibracionToLaboratorioCalibracionRawValue({
      ...this.getFormDefaults(),
      ...laboratorioCalibracion,
    });
    form.reset({
      ...laboratorioCalibracionRawValue,
      id: { value: laboratorioCalibracionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): LaboratorioCalibracionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertLaboratorioCalibracionRawValueToLaboratorioCalibracion(
    rawLaboratorioCalibracion: LaboratorioCalibracionFormRawValue | NewLaboratorioCalibracionFormRawValue,
  ): ILaboratorioCalibracion | NewLaboratorioCalibracion {
    return {
      ...rawLaboratorioCalibracion,
      fechaCreacion: dayjs(rawLaboratorioCalibracion.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawLaboratorioCalibracion.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertLaboratorioCalibracionToLaboratorioCalibracionRawValue(
    laboratorioCalibracion: ILaboratorioCalibracion | (Partial<NewLaboratorioCalibracion> & LaboratorioCalibracionFormDefaults),
  ): LaboratorioCalibracionFormRawValue | PartialWithRequiredKeyOf<NewLaboratorioCalibracionFormRawValue> {
    return {
      ...laboratorioCalibracion,
      fechaCreacion: laboratorioCalibracion.fechaCreacion ? laboratorioCalibracion.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: laboratorioCalibracion.fechaModificacion
        ? laboratorioCalibracion.fechaModificacion.format(DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
