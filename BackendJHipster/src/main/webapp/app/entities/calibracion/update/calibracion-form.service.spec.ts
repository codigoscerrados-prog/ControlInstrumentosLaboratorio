import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../calibracion.test-samples';

import { CalibracionFormService } from './calibracion-form.service';

describe('Calibracion Form Service', () => {
  let service: CalibracionFormService;

  beforeEach(() => {
    service = TestBed.inject(CalibracionFormService);
  });

  describe('Service methods', () => {
    describe('createCalibracionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCalibracionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaEnvio: expect.any(Object),
            fechaCalibracion: expect.any(Object),
            fechaRetorno: expect.any(Object),
            fechaVencimiento: expect.any(Object),
            numeroCertificado: expect.any(Object),
            resultado: expect.any(Object),
            observaciones: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            activo: expect.any(Object),
            laboratorio: expect.any(Object),
          }),
        );
      });

      it('passing ICalibracion should create a new form with FormGroup', () => {
        const formGroup = service.createCalibracionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaEnvio: expect.any(Object),
            fechaCalibracion: expect.any(Object),
            fechaRetorno: expect.any(Object),
            fechaVencimiento: expect.any(Object),
            numeroCertificado: expect.any(Object),
            resultado: expect.any(Object),
            observaciones: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
            activo: expect.any(Object),
            laboratorio: expect.any(Object),
          }),
        );
      });
    });

    describe('getCalibracion', () => {
      it('should return NewCalibracion for default Calibracion initial value', () => {
        const formGroup = service.createCalibracionFormGroup(sampleWithNewData);

        const calibracion = service.getCalibracion(formGroup);

        expect(calibracion).toMatchObject(sampleWithNewData);
      });

      it('should return NewCalibracion for empty Calibracion initial value', () => {
        const formGroup = service.createCalibracionFormGroup();

        const calibracion = service.getCalibracion(formGroup);

        expect(calibracion).toMatchObject({});
      });

      it('should return ICalibracion', () => {
        const formGroup = service.createCalibracionFormGroup(sampleWithRequiredData);

        const calibracion = service.getCalibracion(formGroup);

        expect(calibracion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICalibracion should not enable id FormControl', () => {
        const formGroup = service.createCalibracionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCalibracion should disable id FormControl', () => {
        const formGroup = service.createCalibracionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
