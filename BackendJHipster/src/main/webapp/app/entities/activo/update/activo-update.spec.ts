import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IGrupoInstrumental } from 'app/entities/grupo-instrumental/grupo-instrumental.model';
import { GrupoInstrumentalService } from 'app/entities/grupo-instrumental/service/grupo-instrumental.service';
import { TipoInstrumentoService } from 'app/entities/tipo-instrumento/service/tipo-instrumento.service';
import { ITipoInstrumento } from 'app/entities/tipo-instrumento/tipo-instrumento.model';
import { IActivo } from '../activo.model';
import { ActivoService } from '../service/activo.service';

import { ActivoFormService } from './activo-form.service';
import { ActivoUpdate } from './activo-update';

describe('Activo Management Update Component', () => {
  let comp: ActivoUpdate;
  let fixture: ComponentFixture<ActivoUpdate>;
  let activatedRoute: ActivatedRoute;
  let activoFormService: ActivoFormService;
  let activoService: ActivoService;
  let grupoInstrumentalService: GrupoInstrumentalService;
  let tipoInstrumentoService: TipoInstrumentoService;

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

    fixture = TestBed.createComponent(ActivoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    activoFormService = TestBed.inject(ActivoFormService);
    activoService = TestBed.inject(ActivoService);
    grupoInstrumentalService = TestBed.inject(GrupoInstrumentalService);
    tipoInstrumentoService = TestBed.inject(TipoInstrumentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call GrupoInstrumental query and add missing value', () => {
      const activo: IActivo = { id: 13823 };
      const grupo: IGrupoInstrumental = { id: 30752 };
      activo.grupo = grupo;

      const grupoInstrumentalCollection: IGrupoInstrumental[] = [{ id: 30752 }];
      vi.spyOn(grupoInstrumentalService, 'query').mockReturnValue(of(new HttpResponse({ body: grupoInstrumentalCollection })));
      const additionalGrupoInstrumentals = [grupo];
      const expectedCollection: IGrupoInstrumental[] = [...additionalGrupoInstrumentals, ...grupoInstrumentalCollection];
      vi.spyOn(grupoInstrumentalService, 'addGrupoInstrumentalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ activo });
      comp.ngOnInit();

      expect(grupoInstrumentalService.query).toHaveBeenCalled();
      expect(grupoInstrumentalService.addGrupoInstrumentalToCollectionIfMissing).toHaveBeenCalledWith(
        grupoInstrumentalCollection,
        ...additionalGrupoInstrumentals.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.grupoInstrumentalsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call TipoInstrumento query and add missing value', () => {
      const activo: IActivo = { id: 13823 };
      const tipoInstrumento: ITipoInstrumento = { id: 2947 };
      activo.tipoInstrumento = tipoInstrumento;

      const tipoInstrumentoCollection: ITipoInstrumento[] = [{ id: 2947 }];
      vi.spyOn(tipoInstrumentoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoInstrumentoCollection })));
      const additionalTipoInstrumentos = [tipoInstrumento];
      const expectedCollection: ITipoInstrumento[] = [...additionalTipoInstrumentos, ...tipoInstrumentoCollection];
      vi.spyOn(tipoInstrumentoService, 'addTipoInstrumentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ activo });
      comp.ngOnInit();

      expect(tipoInstrumentoService.query).toHaveBeenCalled();
      expect(tipoInstrumentoService.addTipoInstrumentoToCollectionIfMissing).toHaveBeenCalledWith(
        tipoInstrumentoCollection,
        ...additionalTipoInstrumentos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.tipoInstrumentosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const activo: IActivo = { id: 13823 };
      const grupo: IGrupoInstrumental = { id: 30752 };
      activo.grupo = grupo;
      const tipoInstrumento: ITipoInstrumento = { id: 2947 };
      activo.tipoInstrumento = tipoInstrumento;

      activatedRoute.data = of({ activo });
      comp.ngOnInit();

      expect(comp.grupoInstrumentalsSharedCollection()).toContainEqual(grupo);
      expect(comp.tipoInstrumentosSharedCollection()).toContainEqual(tipoInstrumento);
      expect(comp.activo).toEqual(activo);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IActivo>();
      const activo = { id: 5352 };
      vi.spyOn(activoFormService, 'getActivo').mockReturnValue(activo);
      vi.spyOn(activoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(activo);
      saveSubject.complete();

      // THEN
      expect(activoFormService.getActivo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(activoService.update).toHaveBeenCalledWith(expect.objectContaining(activo));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IActivo>();
      const activo = { id: 5352 };
      vi.spyOn(activoFormService, 'getActivo').mockReturnValue({ id: null });
      vi.spyOn(activoService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(activo);
      saveSubject.complete();

      // THEN
      expect(activoFormService.getActivo).toHaveBeenCalled();
      expect(activoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IActivo>();
      const activo = { id: 5352 };
      vi.spyOn(activoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(activoService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGrupoInstrumental', () => {
      it('should forward to grupoInstrumentalService', () => {
        const entity = { id: 30752 };
        const entity2 = { id: 31278 };
        vi.spyOn(grupoInstrumentalService, 'compareGrupoInstrumental');
        comp.compareGrupoInstrumental(entity, entity2);
        expect(grupoInstrumentalService.compareGrupoInstrumental).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTipoInstrumento', () => {
      it('should forward to tipoInstrumentoService', () => {
        const entity = { id: 2947 };
        const entity2 = { id: 1504 };
        vi.spyOn(tipoInstrumentoService, 'compareTipoInstrumento');
        comp.compareTipoInstrumento(entity, entity2);
        expect(tipoInstrumentoService.compareTipoInstrumento).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
