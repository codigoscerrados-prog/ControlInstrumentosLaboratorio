import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../grupo-instrumental.test-samples';

import { GrupoInstrumentalFormService } from './grupo-instrumental-form.service';

describe('GrupoInstrumental Form Service', () => {
  let service: GrupoInstrumentalFormService;

  beforeEach(() => {
    service = TestBed.inject(GrupoInstrumentalFormService);
  });

  describe('Service methods', () => {
    describe('createGrupoInstrumentalFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGrupoInstrumentalFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
          }),
        );
      });

      it('passing IGrupoInstrumental should create a new form with FormGroup', () => {
        const formGroup = service.createGrupoInstrumentalFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
          }),
        );
      });
    });

    describe('getGrupoInstrumental', () => {
      it('should return NewGrupoInstrumental for default GrupoInstrumental initial value', () => {
        const formGroup = service.createGrupoInstrumentalFormGroup(sampleWithNewData);

        const grupoInstrumental = service.getGrupoInstrumental(formGroup);

        expect(grupoInstrumental).toMatchObject(sampleWithNewData);
      });

      it('should return NewGrupoInstrumental for empty GrupoInstrumental initial value', () => {
        const formGroup = service.createGrupoInstrumentalFormGroup();

        const grupoInstrumental = service.getGrupoInstrumental(formGroup);

        expect(grupoInstrumental).toMatchObject({});
      });

      it('should return IGrupoInstrumental', () => {
        const formGroup = service.createGrupoInstrumentalFormGroup(sampleWithRequiredData);

        const grupoInstrumental = service.getGrupoInstrumental(formGroup);

        expect(grupoInstrumental).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGrupoInstrumental should not enable id FormControl', () => {
        const formGroup = service.createGrupoInstrumentalFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGrupoInstrumental should disable id FormControl', () => {
        const formGroup = service.createGrupoInstrumentalFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
