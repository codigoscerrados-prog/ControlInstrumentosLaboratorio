import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IRol, NewRol } from '../rol.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRol for edit and NewRolFormGroupInput for create.
 */
type RolFormGroupInput = IRol | PartialWithRequiredKeyOf<NewRol>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRol | NewRol> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type RolFormRawValue = FormValueOf<IRol>;

type NewRolFormRawValue = FormValueOf<NewRol>;

type RolFormDefaults = Pick<NewRol, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type RolFormGroupContent = {
  id: FormControl<RolFormRawValue['id'] | NewRol['id']>;
  nombre: FormControl<RolFormRawValue['nombre']>;
  descripcion: FormControl<RolFormRawValue['descripcion']>;
  fechaCreacion: FormControl<RolFormRawValue['fechaCreacion']>;
  usuarioCreacion: FormControl<RolFormRawValue['usuarioCreacion']>;
  fechaModificacion: FormControl<RolFormRawValue['fechaModificacion']>;
  usuarioModificacion: FormControl<RolFormRawValue['usuarioModificacion']>;
};

export type RolFormGroup = FormGroup<RolFormGroupContent>;

@Service()
export class RolFormService {
  createRolFormGroup(rol?: RolFormGroupInput): RolFormGroup {
    const rolRawValue = this.convertRolToRolRawValue({
      ...this.getFormDefaults(),
      ...(rol ?? { id: null }),
    });

    return new FormGroup<RolFormGroupContent>({
      id: new FormControl(
        { value: rolRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(rolRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      descripcion: new FormControl(rolRawValue.descripcion, {
        validators: [Validators.maxLength(150)],
      }),
      fechaCreacion: new FormControl(rolRawValue.fechaCreacion),
      usuarioCreacion: new FormControl(rolRawValue.usuarioCreacion, {
        validators: [Validators.maxLength(50)],
      }),
      fechaModificacion: new FormControl(rolRawValue.fechaModificacion),
      usuarioModificacion: new FormControl(rolRawValue.usuarioModificacion, {
        validators: [Validators.maxLength(50)],
      }),
    });
  }

  getRol(form: RolFormGroup): IRol | NewRol {
    return this.convertRolRawValueToRol(form.getRawValue());
  }

  resetForm(form: RolFormGroup, rol: RolFormGroupInput): void {
    const rolRawValue = this.convertRolToRolRawValue({ ...this.getFormDefaults(), ...rol });
    form.reset({
      ...rolRawValue,
      id: { value: rolRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): RolFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertRolRawValueToRol(rawRol: RolFormRawValue | NewRolFormRawValue): IRol | NewRol {
    return {
      ...rawRol,
      fechaCreacion: dayjs(rawRol.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawRol.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertRolToRolRawValue(
    rol: IRol | (Partial<NewRol> & RolFormDefaults),
  ): RolFormRawValue | PartialWithRequiredKeyOf<NewRolFormRawValue> {
    return {
      ...rol,
      fechaCreacion: rol.fechaCreacion ? rol.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: rol.fechaModificacion ? rol.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
