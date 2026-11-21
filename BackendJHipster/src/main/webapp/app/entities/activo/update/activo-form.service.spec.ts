import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../activo.test-samples';

import { ActivoFormService } from './activo-form.service';

describe('Activo Form Service', () => {
  let service: ActivoFormService;

  beforeEach(() => {
    service = TestBed.inject(ActivoFormService);
  });

  describe('Service methods', () => {
    describe('createActivoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createActivoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            nombre: expect.any(Object),
            marca: expect.any(Object),
            modelo: expect.any(Object),
            numeroSerie: expect.any(Object),
            rango: expect.any(Object),
            resolucion: expect.any(Object),
            ubicacion: expect.any(Object),
            estado: expect.any(Object),
            observaciones: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            grupo: expect.any(Object),
            tipoInstrumento: expect.any(Object),
          }),
        );
      });

      it('passing IActivo should create a new form with FormGroup', () => {
        const formGroup = service.createActivoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            nombre: expect.any(Object),
            marca: expect.any(Object),
            modelo: expect.any(Object),
            numeroSerie: expect.any(Object),
            rango: expect.any(Object),
            resolucion: expect.any(Object),
            ubicacion: expect.any(Object),
            estado: expect.any(Object),
            observaciones: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            grupo: expect.any(Object),
            tipoInstrumento: expect.any(Object),
          }),
        );
      });
    });

    describe('getActivo', () => {
      it('should return NewActivo for default Activo initial value', () => {
        const formGroup = service.createActivoFormGroup(sampleWithNewData);

        const activo = service.getActivo(formGroup);

        expect(activo).toMatchObject(sampleWithNewData);
      });

      it('should return NewActivo for empty Activo initial value', () => {
        const formGroup = service.createActivoFormGroup();

        const activo = service.getActivo(formGroup);

        expect(activo).toMatchObject({});
      });

      it('should return IActivo', () => {
        const formGroup = service.createActivoFormGroup(sampleWithRequiredData);

        const activo = service.getActivo(formGroup);

        expect(activo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IActivo should not enable id FormControl', () => {
        const formGroup = service.createActivoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewActivo should disable id FormControl', () => {
        const formGroup = service.createActivoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
