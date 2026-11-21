import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IActivo, NewActivo } from '../activo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IActivo for edit and NewActivoFormGroupInput for create.
 */
type ActivoFormGroupInput = IActivo | PartialWithRequiredKeyOf<NewActivo>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IActivo | NewActivo> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

type ActivoFormRawValue = FormValueOf<IActivo>;

type NewActivoFormRawValue = FormValueOf<NewActivo>;

type ActivoFormDefaults = Pick<NewActivo, 'id' | 'fechaCreacion' | 'fechaModificacion'>;

type ActivoFormGroupContent = {
  id: FormControl<ActivoFormRawValue['id'] | NewActivo['id']>;
  codigo: FormControl<ActivoFormRawValue['codigo']>;
  nombre: FormControl<ActivoFormRawValue['nombre']>;
  marca: FormControl<ActivoFormRawValue['marca']>;
  modelo: FormControl<ActivoFormRawValue['modelo']>;
  numeroSerie: FormControl<ActivoFormRawValue['numeroSerie']>;
  rango: FormControl<ActivoFormRawValue['rango']>;
  resolucion: FormControl<ActivoFormRawValue['resolucion']>;
  ubicacion: FormControl<ActivoFormRawValue['ubicacion']>;
  estado: FormControl<ActivoFormRawValue['estado']>;
  observaciones: FormControl<ActivoFormRawValue['observaciones']>;
  fechaCreacion: FormControl<ActivoFormRawValue['fechaCreacion']>;
  usuarioCreacion: FormControl<ActivoFormRawValue['usuarioCreacion']>;
  fechaModificacion: FormControl<ActivoFormRawValue['fechaModificacion']>;
  usuarioModificacion: FormControl<ActivoFormRawValue['usuarioModificacion']>;
  grupo: FormControl<ActivoFormRawValue['grupo']>;
  tipoInstrumento: FormControl<ActivoFormRawValue['tipoInstrumento']>;
};

export type ActivoFormGroup = FormGroup<ActivoFormGroupContent>;

@Service()
export class ActivoFormService {
  createActivoFormGroup(activo?: ActivoFormGroupInput): ActivoFormGroup {
    const activoRawValue = this.convertActivoToActivoRawValue({
      ...this.getFormDefaults(),
      ...(activo ?? { id: null }),
    });

    return new FormGroup<ActivoFormGroupContent>({
      id: new FormControl(
        { value: activoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(activoRawValue.codigo, {
        validators: [Validators.required, Validators.maxLength(40)],
      }),
      nombre: new FormControl(activoRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      marca: new FormControl(activoRawValue.marca, {
        validators: [Validators.maxLength(100)],
      }),
      modelo: new FormControl(activoRawValue.modelo, {
        validators: [Validators.maxLength(100)],
      }),
      numeroSerie: new FormControl(activoRawValue.numeroSerie, {
        validators: [Validators.maxLength(100)],
      }),
      rango: new FormControl(activoRawValue.rango, {
        validators: [Validators.maxLength(100)],
      }),
      resolucion: new FormControl(activoRawValue.resolucion, {
        validators: [Validators.maxLength(100)],
      }),
      ubicacion: new FormControl(activoRawValue.ubicacion, {
        validators: [Validators.maxLength(150)],
      }),
      estado: new FormControl(activoRawValue.estado, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      observaciones: new FormControl(activoRawValue.observaciones, {
        validators: [Validators.maxLength(500)],
      }),
      fechaCreacion: new FormControl(activoRawValue.fechaCreacion),
      usuarioCreacion: new FormControl(activoRawValue.usuarioCreacion, {
        validators: [Validators.maxLength(50)],
      }),
      fechaModificacion: new FormControl(activoRawValue.fechaModificacion),
      usuarioModificacion: new FormControl(activoRawValue.usuarioModificacion, {
        validators: [Validators.maxLength(50)],
      }),
      grupo: new FormControl(activoRawValue.grupo, {
        validators: [Validators.required],
      }),
      tipoInstrumento: new FormControl(activoRawValue.tipoInstrumento, {
        validators: [Validators.required],
      }),
    });
  }

  getActivo(form: ActivoFormGroup): IActivo | NewActivo {
    return this.convertActivoRawValueToActivo(form.getRawValue());
  }

  resetForm(form: ActivoFormGroup, activo: ActivoFormGroupInput): void {
    const activoRawValue = this.convertActivoToActivoRawValue({ ...this.getFormDefaults(), ...activo });
    form.reset({
      ...activoRawValue,
      id: { value: activoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ActivoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaModificacion: currentTime,
    };
  }

  private convertActivoRawValueToActivo(rawActivo: ActivoFormRawValue | NewActivoFormRawValue): IActivo | NewActivo {
    return {
      ...rawActivo,
      fechaCreacion: dayjs(rawActivo.fechaCreacion, DATE_TIME_FORMAT),
      fechaModificacion: dayjs(rawActivo.fechaModificacion, DATE_TIME_FORMAT),
    };
  }

  private convertActivoToActivoRawValue(
    activo: IActivo | (Partial<NewActivo> & ActivoFormDefaults),
  ): ActivoFormRawValue | PartialWithRequiredKeyOf<NewActivoFormRawValue> {
    return {
      ...activo,
      fechaCreacion: activo.fechaCreacion ? activo.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaModificacion: activo.fechaModificacion ? activo.fechaModificacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
