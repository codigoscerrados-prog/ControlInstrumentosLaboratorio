import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { TipoInstrumentoService } from '../service/tipo-instrumento.service';
import { ITipoInstrumento } from '../tipo-instrumento.model';

import { TipoInstrumentoFormService } from './tipo-instrumento-form.service';
import { TipoInstrumentoUpdate } from './tipo-instrumento-update';

describe('TipoInstrumento Management Update Component', () => {
  let comp: TipoInstrumentoUpdate;
  let fixture: ComponentFixture<TipoInstrumentoUpdate>;
  let activatedRoute: ActivatedRoute;
  let tipoInstrumentoFormService: TipoInstrumentoFormService;
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

    fixture = TestBed.createComponent(TipoInstrumentoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoInstrumentoFormService = TestBed.inject(TipoInstrumentoFormService);
    tipoInstrumentoService = TestBed.inject(TipoInstrumentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const tipoInstrumento: ITipoInstrumento = { id: 1504 };

      activatedRoute.data = of({ tipoInstrumento });
      comp.ngOnInit();

      expect(comp.tipoInstrumento).toEqual(tipoInstrumento);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITipoInstrumento>();
      const tipoInstrumento = { id: 2947 };
      vi.spyOn(tipoInstrumentoFormService, 'getTipoInstrumento').mockReturnValue(tipoInstrumento);
      vi.spyOn(tipoInstrumentoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoInstrumento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(tipoInstrumento);
      saveSubject.complete();

      // THEN
      expect(tipoInstrumentoFormService.getTipoInstrumento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoInstrumentoService.update).toHaveBeenCalledWith(expect.objectContaining(tipoInstrumento));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITipoInstrumento>();
      const tipoInstrumento = { id: 2947 };
      vi.spyOn(tipoInstrumentoFormService, 'getTipoInstrumento').mockReturnValue({ id: null });
      vi.spyOn(tipoInstrumentoService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoInstrumento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(tipoInstrumento);
      saveSubject.complete();

      // THEN
      expect(tipoInstrumentoFormService.getTipoInstrumento).toHaveBeenCalled();
      expect(tipoInstrumentoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITipoInstrumento>();
      const tipoInstrumento = { id: 2947 };
      vi.spyOn(tipoInstrumentoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoInstrumento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoInstrumentoService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
