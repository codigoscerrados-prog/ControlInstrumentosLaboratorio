import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { ICalibracion, NewCalibracion } from '../calibracion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICalibracion for edit and NewCalibracionFormGroupInput for create.
 */
type CalibracionFormGroupInput = ICalibracion | PartialWithRequiredKeyOf<NewCalibracion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICalibracion | NewCalibracion> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type CalibracionFormRawValue = FormValueOf<ICalibracion>;

type NewCalibracionFormRawValue = FormValueOf<NewCalibracion>;

type CalibracionFormDefaults = Pick<NewCalibracion, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type CalibracionFormGroupContent = {
  id: FormControl<CalibracionFormRawValue['id'] | NewCalibracion['id']>;
  fechaEnvio: FormControl<CalibracionFormRawValue['fechaEnvio']>;
  fechaCalibracion: FormControl<CalibracionFormRawValue['fechaCalibracion']>;
  fechaRetorno: FormControl<CalibracionFormRawValue['fechaRetorno']>;
  fechaVencimiento: FormControl<CalibracionFormRawValue['fechaVencimiento']>;
  numeroCertificado: FormControl<CalibracionFormRawValue['numeroCertificado']>;
  resultado: FormControl<CalibracionFormRawValue['resultado']>;
  observaciones: FormControl<CalibracionFormRawValue['observaciones']>;
  fechaCreacion: FormControl<CalibracionFormRawValue['fechaCreacion']>;
  usuarioCreacion: FormControl<CalibracionFormRawValue['usuarioCreacion']>;
  fechaModificacion: FormControl<CalibracionFormRawValue['fechaModificacion']>;
  usuarioModificacion: FormControl<CalibracionFormRawValue['usuarioModificacion']>;
  activo: FormControl<CalibracionFormRawValue['activo']>;
  laboratorio: FormControl<CalibracionFormRawValue['laboratorio']>;
};

export type CalibracionFormGroup = FormGroup<CalibracionFormGroupContent>;

@Service()
export class CalibracionFormService {
  createCalibracionFormGroup(calibracion?: CalibracionFormGroupInput): CalibracionFormGroup {
    const calibracionRawValue = this.convertCalibracionToCalibracionRawValue({
      ...this.getFormDefaults(),
      ...(calibracion ?? { id: null }),
    });

    return new FormGroup<CalibracionFormGroupContent>({
      id: new FormControl(
        { value: calibracionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fechaEnvio: new FormControl(calibracionRawValue.fechaEnvio),
      fechaCalibracion: new FormControl(calibracionRawValue.fechaCalibracion),
      fechaRetorno: new FormControl(calibracionRawValue.fechaRetorno),
      fechaVencimiento: new FormControl(calibracionRawValue.fechaVencimiento),
      numeroCertificado: new FormControl(calibracionRawValue.numeroCertificado, {
        validators: [Validators.maxLength(100)],
      }),
      resultado: new FormControl(calibracionRawValue.resultado, {
        validators: [Validators.maxLength(50)],
      }),
      observaciones: new FormControl(calibracionRawValue.observaciones, {
        validators: [Validators.maxLength(500)],
      }),
      fechaCreacion: new FormControl(calibracionRawValue.fechaCreacion),
      usuarioCreacion: new FormControl(calibracionRawValue.usuarioCreacion, {
        validators: [Validators.maxLength(50)],
      }),
      fechaModificacion: new FormControl(calibracionRawValue.fechaModificacion),
      usuarioModificacion: new FormControl(calibracionRawValue.usuarioModificacion, {
        validators: [Validators.maxLength(50)],
      }),
      activo: new FormControl(calibracionRawValue.activo, {
        validators: [Validators.required],
      }),
      laboratorio: new FormControl(calibracionRawValue.laboratorio, {
        validators: [Validators.required],
      }),
    });
  }

  getCalibracion(form: CalibracionFormGroup): ICalibracion | NewCalibracion {
    return this.convertCalibracionRawValueToCalibracion(form.getRawValue());
  }

  resetForm(form: CalibracionFormGroup, calibracion: CalibracionFormGroupInput): void {
    const calibracionRawValue = this.convertCalibracionToCalibracionRawValue({ ...this.getFormDefaults(), ...calibracion });
    form.reset({
      ...calibracionRawValue,
      id: { value: calibracionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CalibracionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertCalibracionRawValueToCalibracion(
    rawCalibracion: CalibracionFormRawValue | NewCalibracionFormRawValue,
  ): ICalibracion | NewCalibracion {
    return {
      ...rawCalibracion,
      fechaCreacion: dayjs(rawCalibracion.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawCalibracion.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertCalibracionToCalibracionRawValue(
    calibracion: ICalibracion | (Partial<NewCalibracion> & CalibracionFormDefaults),
  ): CalibracionFormRawValue | PartialWithRequiredKeyOf<NewCalibracionFormRawValue> {
    return {
      ...calibracion,
      fechaCreacion: calibracion.fechaCreacion ? calibracion.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: calibracion.fechaModificacion ? calibracion.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
