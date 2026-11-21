import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ILaboratorioCalibracion } from '../laboratorio-calibracion.model';
import { LaboratorioCalibracionService } from '../service/laboratorio-calibracion.service';

import { LaboratorioCalibracionFormService } from './laboratorio-calibracion-form.service';
import { LaboratorioCalibracionUpdate } from './laboratorio-calibracion-update';

describe('LaboratorioCalibracion Management Update Component', () => {
  let comp: LaboratorioCalibracionUpdate;
  let fixture: ComponentFixture<LaboratorioCalibracionUpdate>;
  let activatedRoute: ActivatedRoute;
  let laboratorioCalibracionFormService: LaboratorioCalibracionFormService;
  let laboratorioCalibracionService: LaboratorioCalibracionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(LaboratorioCalibracionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    laboratorioCalibracionFormService = TestBed.inject(LaboratorioCalibracionFormService);
    laboratorioCalibracionService = TestBed.inject(LaboratorioCalibracionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const laboratorioCalibracion: ILaboratorioCalibracion = { id: 14709 };

      activatedRoute.data = of({ laboratorioCalibracion });
      comp.ngOnInit();

      expect(comp.laboratorioCalibracion).toEqual(laboratorioCalibracion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILaboratorioCalibracion>();
      const laboratorioCalibracion = { id: 6359 };
      vi.spyOn(laboratorioCalibracionFormService, 'getLaboratorioCalibracion').mockReturnValue(laboratorioCalibracion);
      vi.spyOn(laboratorioCalibracionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ laboratorioCalibracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(laboratorioCalibracion);
      saveSubject.complete();

      // THEN
      expect(laboratorioCalibracionFormService.getLaboratorioCalibracion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(laboratorioCalibracionService.update).toHaveBeenCalledWith(expect.objectContaining(laboratorioCalibracion));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ILaboratorioCalibracion>();
      const laboratorioCalibracion = { id: 6359 };
      vi.spyOn(laboratorioCalibracionFormService, 'getLaboratorioCalibracion').mockReturnValue({ id: null });
      vi.spyOn(laboratorioCalibracionService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ laboratorioCalibracion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(laboratorioCalibracion);
      saveSubject.complete();

      // THEN
      expect(laboratorioCalibracionFormService.getLaboratorioCalibracion).toHaveBeenCalled();
      expect(laboratorioCalibracionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ILaboratorioCalibracion>();
      const laboratorioCalibracion = { id: 6359 };
      vi.spyOn(laboratorioCalibracionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ laboratorioCalibracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(laboratorioCalibracionService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
