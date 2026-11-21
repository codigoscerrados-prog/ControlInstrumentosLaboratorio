import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../laboratorio-calibracion.test-samples';

import { LaboratorioCalibracionFormService } from './laboratorio-calibracion-form.service';

describe('LaboratorioCalibracion Form Service', () => {
  let service: LaboratorioCalibracionFormService;

  beforeEach(() => {
    service = TestBed.inject(LaboratorioCalibracionFormService);
  });

  describe('Service methods', () => {
    describe('createLaboratorioCalibracionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLaboratorioCalibracionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            ruc: expect.any(Object),
            direccion: expect.any(Object),
            telefono: expect.any(Object),
            correo: expect.any(Object),
            estado: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
          }),
        );
      });

      it('passing ILaboratorioCalibracion should create a new form with FormGroup', () => {
        const formGroup = service.createLaboratorioCalibracionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            ruc: expect.any(Object),
            direccion: expect.any(Object),
            telefono: expect.any(Object),
            correo: expect.any(Object),
            estado: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
          }),
        );
      });
    });

    describe('getLaboratorioCalibracion', () => {
      it('should return NewLaboratorioCalibracion for default LaboratorioCalibracion initial value', () => {
        const formGroup = service.createLaboratorioCalibracionFormGroup(sampleWithNewData);

        const laboratorioCalibracion = service.getLaboratorioCalibracion(formGroup);

        expect(laboratorioCalibracion).toMatchObject(sampleWithNewData);
      });

      it('should return NewLaboratorioCalibracion for empty LaboratorioCalibracion initial value', () => {
        const formGroup = service.createLaboratorioCalibracionFormGroup();

        const laboratorioCalibracion = service.getLaboratorioCalibracion(formGroup);

        expect(laboratorioCalibracion).toMatchObject({});
      });

      it('should return ILaboratorioCalibracion', () => {
        const formGroup = service.createLaboratorioCalibracionFormGroup(sampleWithRequiredData);

        const laboratorioCalibracion = service.getLaboratorioCalibracion(formGroup);

        expect(laboratorioCalibracion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILaboratorioCalibracion should not enable id FormControl', () => {
        const formGroup = service.createLaboratorioCalibracionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLaboratorioCalibracion should disable id FormControl', () => {
        const formGroup = service.createLaboratorioCalibracionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
