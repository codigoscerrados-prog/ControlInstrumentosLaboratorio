import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IRecuperacionPassword, NewRecuperacionPassword } from '../recuperacion-password.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecuperacionPassword for edit and NewRecuperacionPasswordFormGroupInput for create.
 */
type RecuperacionPasswordFormGroupInput = IRecuperacionPassword | PartialWithRequiredKeyOf<NewRecuperacionPassword>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRecuperacionPassword | NewRecuperacionPassword> = Omit<T, 'fechaSolicitud' | 'fechaExpiracion'> & {
  fechaSolicitud?: string | null;
  fechaExpiracion?: string | null;
};

type RecuperacionPasswordFormRawValue = FormValueOf<IRecuperacionPassword>;

type NewRecuperacionPasswordFormRawValue = FormValueOf<NewRecuperacionPassword>;

type RecuperacionPasswordFormDefaults = Pick<NewRecuperacionPassword, 'id' | 'fechaSolicitud' | 'fechaExpiracion' | 'usado'>;

type RecuperacionPasswordFormGroupContent = {
  id: FormControl<RecuperacionPasswordFormRawValue['id'] | NewRecuperacionPassword['id']>;
  token: FormControl<RecuperacionPasswordFormRawValue['token']>;
  fechaSolicitud: FormControl<RecuperacionPasswordFormRawValue['fechaSolicitud']>;
  fechaExpiracion: FormControl<RecuperacionPasswordFormRawValue['fechaExpiracion']>;
  usado: FormControl<RecuperacionPasswordFormRawValue['usado']>;
  usuario: FormControl<RecuperacionPasswordFormRawValue['usuario']>;
};

export type RecuperacionPasswordFormGroup = FormGroup<RecuperacionPasswordFormGroupContent>;

@Service()
export class RecuperacionPasswordFormService {
  createRecuperacionPasswordFormGroup(recuperacionPassword?: RecuperacionPasswordFormGroupInput): RecuperacionPasswordFormGroup {
    const recuperacionPasswordRawValue = this.convertRecuperacionPasswordToRecuperacionPasswordRawValue({
      ...this.getFormDefaults(),
      ...(recuperacionPassword ?? { id: null }),
    });

    return new FormGroup<RecuperacionPasswordFormGroupContent>({
      id: new FormControl(
        { value: recuperacionPasswordRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      token: new FormControl(recuperacionPasswordRawValue.token, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      fechaSolicitud: new FormControl(recuperacionPasswordRawValue.fechaSolicitud, {
        validators: [Validators.required],
      }),
      fechaExpiracion: new FormControl(recuperacionPasswordRawValue.fechaExpiracion, {
        validators: [Validators.required],
      }),
      usado: new FormControl(recuperacionPasswordRawValue.usado, {
        validators: [Validators.required],
      }),
      usuario: new FormControl(recuperacionPasswordRawValue.usuario, {
        validators: [Validators.required],
      }),
    });
  }

  getRecuperacionPassword(form: RecuperacionPasswordFormGroup): IRecuperacionPassword | NewRecuperacionPassword {
    return this.convertRecuperacionPasswordRawValueToRecuperacionPassword(form.getRawValue());
  }

  resetForm(form: RecuperacionPasswordFormGroup, recuperacionPassword: RecuperacionPasswordFormGroupInput): void {
    const recuperacionPasswordRawValue = this.convertRecuperacionPasswordToRecuperacionPasswordRawValue({
      ...this.getFormDefaults(),
      ...recuperacionPassword,
    });
    form.reset({
      ...recuperacionPasswordRawValue,
      id: { value: recuperacionPasswordRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): RecuperacionPasswordFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaSolicitud: currentTime,
      fechaExpiracion: currentTime,
      usado: false,
    };
  }

  private convertRecuperacionPasswordRawValueToRecuperacionPassword(
    rawRecuperacionPassword: RecuperacionPasswordFormRawValue | NewRecuperacionPasswordFormRawValue,
  ): IRecuperacionPassword | NewRecuperacionPassword {
    return {
      ...rawRecuperacionPassword,
      fechaSolicitud: dayjs(rawRecuperacionPassword.fechaSolicitud, DATE_TIME_FORMAT),
      fechaExpiracion: dayjs(rawRecuperacionPassword.fechaExpiracion, DATE_TIME_FORMAT),
    };
  }

  private convertRecuperacionPasswordToRecuperacionPasswordRawValue(
    recuperacionPassword: IRecuperacionPassword | (Partial<NewRecuperacionPassword> & RecuperacionPasswordFormDefaults),
  ): RecuperacionPasswordFormRawValue | PartialWithRequiredKeyOf<NewRecuperacionPasswordFormRawValue> {
    return {
      ...recuperacionPassword,
      fechaSolicitud: recuperacionPassword.fechaSolicitud ? recuperacionPassword.fechaSolicitud.format(DATE_TIME_FORMAT) : undefined,
      fechaExpiracion: recuperacionPassword.fechaExpiracion ? recuperacionPassword.fechaExpiracion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
