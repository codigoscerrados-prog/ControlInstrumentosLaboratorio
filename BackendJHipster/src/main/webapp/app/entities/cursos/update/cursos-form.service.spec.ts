import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cursos.test-samples';

import { CursosFormService } from './cursos-form.service';

describe('Cursos Form Service', () => {
  let service: CursosFormService;

  beforeEach(() => {
    service = TestBed.inject(CursosFormService);
  });

  describe('Service methods', () => {
    describe('createCursosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCursosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            curso: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuairoModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
          }),
        );
      });

      it('passing ICursos should create a new form with FormGroup', () => {
        const formGroup = service.createCursosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            curso: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuairoModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
          }),
        );
      });
    });

    describe('getCursos', () => {
      it('should return NewCursos for default Cursos initial value', () => {
        const formGroup = service.createCursosFormGroup(sampleWithNewData);

        const cursos = service.getCursos(formGroup);

        expect(cursos).toMatchObject(sampleWithNewData);
      });

      it('should return NewCursos for empty Cursos initial value', () => {
        const formGroup = service.createCursosFormGroup();

        const cursos = service.getCursos(formGroup);

        expect(cursos).toMatchObject({});
      });

      it('should return ICursos', () => {
        const formGroup = service.createCursosFormGroup(sampleWithRequiredData);

        const cursos = service.getCursos(formGroup);

        expect(cursos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICursos should not enable id FormControl', () => {
        const formGroup = service.createCursosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCursos should disable id FormControl', () => {
        const formGroup = service.createCursosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
