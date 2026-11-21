import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tipo-instrumento.test-samples';

import { TipoInstrumentoFormService } from './tipo-instrumento-form.service';

describe('TipoInstrumento Form Service', () => {
  let service: TipoInstrumentoFormService;

  beforeEach(() => {
    service = TestBed.inject(TipoInstrumentoFormService);
  });

  describe('Service methods', () => {
    describe('createTipoInstrumentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoInstrumentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            fechaCreacion: expect.any(Object),
            usuarioCreacion: expect.any(Object),
            fechaModificacion: expect.any(Object),
            usuarioModificacion: expect.any(Object),
          }),
        );
      });

      it('passing ITipoInstrumento should create a new form with FormGroup', () => {
        const formGroup = service.createTipoInstrumentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
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

    describe('getTipoInstrumento', () => {
      it('should return NewTipoInstrumento for default TipoInstrumento initial value', () => {
        const formGroup = service.createTipoInstrumentoFormGroup(sampleWithNewData);

        const tipoInstrumento = service.getTipoInstrumento(formGroup);

        expect(tipoInstrumento).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoInstrumento for empty TipoInstrumento initial value', () => {
        const formGroup = service.createTipoInstrumentoFormGroup();

        const tipoInstrumento = service.getTipoInstrumento(formGroup);

        expect(tipoInstrumento).toMatchObject({});
      });

      it('should return ITipoInstrumento', () => {
        const formGroup = service.createTipoInstrumentoFormGroup(sampleWithRequiredData);

        const tipoInstrumento = service.getTipoInstrumento(formGroup);

        expect(tipoInstrumento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoInstrumento should not enable id FormControl', () => {
        const formGroup = service.createTipoInstrumentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoInstrumento should disable id FormControl', () => {
        const formGroup = service.createTipoInstrumentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
