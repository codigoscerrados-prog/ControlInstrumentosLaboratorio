import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IActivo } from 'app/entities/activo/activo.model';
import { ActivoService } from 'app/entities/activo/service/activo.service';
import { ILaboratorioCalibracion } from 'app/entities/laboratorio-calibracion/laboratorio-calibracion.model';
import { LaboratorioCalibracionService } from 'app/entities/laboratorio-calibracion/service/laboratorio-calibracion.service';
import { ICalibracion } from '../calibracion.model';
import { CalibracionService } from '../service/calibracion.service';

import { CalibracionFormService } from './calibracion-form.service';
import { CalibracionUpdate } from './calibracion-update';

describe('Calibracion Management Update Component', () => {
  let comp: CalibracionUpdate;
  let fixture: ComponentFixture<CalibracionUpdate>;
  let activatedRoute: ActivatedRoute;
  let calibracionFormService: CalibracionFormService;
  let calibracionService: CalibracionService;
  let activoService: ActivoService;
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

    fixture = TestBed.createComponent(CalibracionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    calibracionFormService = TestBed.inject(CalibracionFormService);
    calibracionService = TestBed.inject(CalibracionService);
    activoService = TestBed.inject(ActivoService);
    laboratorioCalibracionService = TestBed.inject(LaboratorioCalibracionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Activo query and add missing value', () => {
      const calibracion: ICalibracion = { id: 29846 };
      const activo: IActivo = { id: 5352 };
      calibracion.activo = activo;

      const activoCollection: IActivo[] = [{ id: 5352 }];
      vi.spyOn(activoService, 'query').mockReturnValue(of(new HttpResponse({ body: activoCollection })));
      const additionalActivos = [activo];
      const expectedCollection: IActivo[] = [...additionalActivos, ...activoCollection];
      vi.spyOn(activoService, 'addActivoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ calibracion });
      comp.ngOnInit();

      expect(activoService.query).toHaveBeenCalled();
      expect(activoService.addActivoToCollectionIfMissing).toHaveBeenCalledWith(
        activoCollection,
        ...additionalActivos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.activosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call LaboratorioCalibracion query and add missing value', () => {
      const calibracion: ICalibracion = { id: 29846 };
      const laboratorio: ILaboratorioCalibracion = { id: 6359 };
      calibracion.laboratorio = laboratorio;

      const laboratorioCalibracionCollection: ILaboratorioCalibracion[] = [{ id: 6359 }];
      vi.spyOn(laboratorioCalibracionService, 'query').mockReturnValue(of(new HttpResponse({ body: laboratorioCalibracionCollection })));
      const additionalLaboratorioCalibracions = [laboratorio];
      const expectedCollection: ILaboratorioCalibracion[] = [...additionalLaboratorioCalibracions, ...laboratorioCalibracionCollection];
      vi.spyOn(laboratorioCalibracionService, 'addLaboratorioCalibracionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ calibracion });
      comp.ngOnInit();

      expect(laboratorioCalibracionService.query).toHaveBeenCalled();
      expect(laboratorioCalibracionService.addLaboratorioCalibracionToCollectionIfMissing).toHaveBeenCalledWith(
        laboratorioCalibracionCollection,
        ...additionalLaboratorioCalibracions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.laboratorioCalibracionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const calibracion: ICalibracion = { id: 29846 };
      const activo: IActivo = { id: 5352 };
      calibracion.activo = activo;
      const laboratorio: ILaboratorioCalibracion = { id: 6359 };
      calibracion.laboratorio = laboratorio;

      activatedRoute.data = of({ calibracion });
      comp.ngOnInit();

      expect(comp.activosSharedCollection()).toContainEqual(activo);
      expect(comp.laboratorioCalibracionsSharedCollection()).toContainEqual(laboratorio);
      expect(comp.calibracion).toEqual(calibracion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICalibracion>();
      const calibracion = { id: 10837 };
      vi.spyOn(calibracionFormService, 'getCalibracion').mockReturnValue(calibracion);
      vi.spyOn(calibracionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calibracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(calibracion);
      saveSubject.complete();

      // THEN
      expect(calibracionFormService.getCalibracion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(calibracionService.update).toHaveBeenCalledWith(expect.objectContaining(calibracion));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICalibracion>();
      const calibracion = { id: 10837 };
      vi.spyOn(calibracionFormService, 'getCalibracion').mockReturnValue({ id: null });
      vi.spyOn(calibracionService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calibracion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(calibracion);
      saveSubject.complete();

      // THEN
      expect(calibracionFormService.getCalibracion).toHaveBeenCalled();
      expect(calibracionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICalibracion>();
      const calibracion = { id: 10837 };
      vi.spyOn(calibracionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ calibracion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(calibracionService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareActivo', () => {
      it('should forward to activoService', () => {
        const entity = { id: 5352 };
        const entity2 = { id: 13823 };
        vi.spyOn(activoService, 'compareActivo');
        comp.compareActivo(entity, entity2);
        expect(activoService.compareActivo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLaboratorioCalibracion', () => {
      it('should forward to laboratorioCalibracionService', () => {
        const entity = { id: 6359 };
        const entity2 = { id: 14709 };
        vi.spyOn(laboratorioCalibracionService, 'compareLaboratorioCalibracion');
        comp.compareLaboratorioCalibracion(entity, entity2);
        expect(laboratorioCalibracionService.compareLaboratorioCalibracion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
