import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../recuperacion-password.test-samples';

import { RecuperacionPasswordFormService } from './recuperacion-password-form.service';

describe('RecuperacionPassword Form Service', () => {
  let service: RecuperacionPasswordFormService;

  beforeEach(() => {
    service = TestBed.inject(RecuperacionPasswordFormService);
  });

  describe('Service methods', () => {
    describe('createRecuperacionPasswordFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecuperacionPasswordFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            token: expect.any(Object),
            fechaSolicitud: expect.any(Object),
            fechaExpiracion: expect.any(Object),
            usado: expect.any(Object),
            usuario: expect.any(Object),
          }),
        );
      });

      it('passing IRecuperacionPassword should create a new form with FormGroup', () => {
        const formGroup = service.createRecuperacionPasswordFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            token: expect.any(Object),
            fechaSolicitud: expect.any(Object),
            fechaExpiracion: expect.any(Object),
            usado: expect.any(Object),
            usuario: expect.any(Object),
          }),
        );
      });
    });

    describe('getRecuperacionPassword', () => {
      it('should return NewRecuperacionPassword for default RecuperacionPassword initial value', () => {
        const formGroup = service.createRecuperacionPasswordFormGroup(sampleWithNewData);

        const recuperacionPassword = service.getRecuperacionPassword(formGroup);

        expect(recuperacionPassword).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecuperacionPassword for empty RecuperacionPassword initial value', () => {
        const formGroup = service.createRecuperacionPasswordFormGroup();

        const recuperacionPassword = service.getRecuperacionPassword(formGroup);

        expect(recuperacionPassword).toMatchObject({});
      });

      it('should return IRecuperacionPassword', () => {
        const formGroup = service.createRecuperacionPasswordFormGroup(sampleWithRequiredData);

        const recuperacionPassword = service.getRecuperacionPassword(formGroup);

        expect(recuperacionPassword).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecuperacionPassword should not enable id FormControl', () => {
        const formGroup = service.createRecuperacionPasswordFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecuperacionPassword should disable id FormControl', () => {
        const formGroup = service.createRecuperacionPasswordFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
