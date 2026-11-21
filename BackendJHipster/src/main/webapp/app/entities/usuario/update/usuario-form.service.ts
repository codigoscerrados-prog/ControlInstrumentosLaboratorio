import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IUsuario, NewUsuario } from '../usuario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUsuario for edit and NewUsuarioFormGroupInput for create.
 */
type UsuarioFormGroupInput = IUsuario | PartialWithRequiredKeyOf<NewUsuario>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUsuario | NewUsuario> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type UsuarioFormRawValue = FormValueOf<IUsuario>;

type NewUsuarioFormRawValue = FormValueOf<NewUsuario>;

type UsuarioFormDefaults = Pick<NewUsuario, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type UsuarioFormGroupContent = {
  id: FormControl<UsuarioFormRawValue['id'] | NewUsuario['id']>;
  username: FormControl<UsuarioFormRawValue['username']>;
  password: FormControl<UsuarioFormRawValue['password']>;
  correo: FormControl<UsuarioFormRawValue['correo']>;
  estado: FormControl<UsuarioFormRawValue['estado']>;
  fechaCreacion: FormControl<UsuarioFormRawValue['fechaCreacion']>;
  usuarioCreacion: FormControl<UsuarioFormRawValue['usuarioCreacion']>;
  fechaModificacion: FormControl<UsuarioFormRawValue['fechaModificacion']>;
  usuarioModificacion: FormControl<UsuarioFormRawValue['usuarioModificacion']>;
  rol: FormControl<UsuarioFormRawValue['rol']>;
};

export type UsuarioFormGroup = FormGroup<UsuarioFormGroupContent>;

@Service()
export class UsuarioFormService {
  createUsuarioFormGroup(usuario?: UsuarioFormGroupInput): UsuarioFormGroup {
    const usuarioRawValue = this.convertUsuarioToUsuarioRawValue({
      ...this.getFormDefaults(),
      ...(usuario ?? { id: null }),
    });

    return new FormGroup<UsuarioFormGroupContent>({
      id: new FormControl(
        { value: usuarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      username: new FormControl(usuarioRawValue.username, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      password: new FormControl(usuarioRawValue.password, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      correo: new FormControl(usuarioRawValue.correo, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      estado: new FormControl(usuarioRawValue.estado, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      fechaCreacion: new FormControl(usuarioRawValue.fechaCreacion),
      usuarioCreacion: new FormControl(usuarioRawValue.usuarioCreacion, {
        validators: [Validators.maxLength(50)],
      }),
      fechaModificacion: new FormControl(usuarioRawValue.fechaModificacion),
      usuarioModificacion: new FormControl(usuarioRawValue.usuarioModificacion, {
        validators: [Validators.maxLength(50)],
      }),
      rol: new FormControl(usuarioRawValue.rol, {
        validators: [Validators.required],
      }),
    });
  }

  getUsuario(form: UsuarioFormGroup): IUsuario | NewUsuario {
    return this.convertUsuarioRawValueToUsuario(form.getRawValue());
  }

  resetForm(form: UsuarioFormGroup, usuario: UsuarioFormGroupInput): void {
    const usuarioRawValue = this.convertUsuarioToUsuarioRawValue({ ...this.getFormDefaults(), ...usuario });
    form.reset({
      ...usuarioRawValue,
      id: { value: usuarioRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): UsuarioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertUsuarioRawValueToUsuario(rawUsuario: UsuarioFormRawValue | NewUsuarioFormRawValue): IUsuario | NewUsuario {
    return {
      ...rawUsuario,
      fechaCreacion: dayjs(rawUsuario.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawUsuario.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertUsuarioToUsuarioRawValue(
    usuario: IUsuario | (Partial<NewUsuario> & UsuarioFormDefaults),
  ): UsuarioFormRawValue | PartialWithRequiredKeyOf<NewUsuarioFormRawValue> {
    return {
      ...usuario,
      fechaCreacion: usuario.fechaCreacion ? usuario.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: usuario.fechaModificacion ? usuario.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
