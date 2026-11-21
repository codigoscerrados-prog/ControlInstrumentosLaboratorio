import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../alumno.test-samples';

import { AlumnoFormService } from './alumno-form.service';

describe('Alumno Form Service', () => {
  let service: AlumnoFormService;

  beforeEach(() => {
    service = TestBed.inject(AlumnoFormService);
  });

  describe('Service methods', () => {
    describe('createAlumnoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAlumnoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigoAlumno: expect.any(Object),
            apellidoP: expect.any(Object),
            apellidoM: expect.any(Object),
            nombre: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuairoModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
          }),
        );
      });

      it('passing IAlumno should create a new form with FormGroup', () => {
        const formGroup = service.createAlumnoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigoAlumno: expect.any(Object),
            apellidoP: expect.any(Object),
            apellidoM: expect.any(Object),
            nombre: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuairoModificacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
          }),
        );
      });
    });

    describe('getAlumno', () => {
      it('should return NewAlumno for default Alumno initial value', () => {
        const formGroup = service.createAlumnoFormGroup(sampleWithNewData);

        const alumno = service.getAlumno(formGroup);

        expect(alumno).toMatchObject(sampleWithNewData);
      });

      it('should return NewAlumno for empty Alumno initial value', () => {
        const formGroup = service.createAlumnoFormGroup();

        const alumno = service.getAlumno(formGroup);

        expect(alumno).toMatchObject({});
      });

      it('should return IAlumno', () => {
        const formGroup = service.createAlumnoFormGroup(sampleWithRequiredData);

        const alumno = service.getAlumno(formGroup);

        expect(alumno).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAlumno should not enable id FormControl', () => {
        const formGroup = service.createAlumnoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAlumno should disable id FormControl', () => {
        const formGroup = service.createAlumnoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
