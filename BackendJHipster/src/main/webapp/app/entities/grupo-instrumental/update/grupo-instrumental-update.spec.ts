import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IGrupoInstrumental } from '../grupo-instrumental.model';
import { GrupoInstrumentalService } from '../service/grupo-instrumental.service';

import { GrupoInstrumentalFormService } from './grupo-instrumental-form.service';
import { GrupoInstrumentalUpdate } from './grupo-instrumental-update';

describe('GrupoInstrumental Management Update Component', () => {
  let comp: GrupoInstrumentalUpdate;
  let fixture: ComponentFixture<GrupoInstrumentalUpdate>;
  let activatedRoute: ActivatedRoute;
  let grupoInstrumentalFormService: GrupoInstrumentalFormService;
  let grupoInstrumentalService: GrupoInstrumentalService;

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

    fixture = TestBed.createComponent(GrupoInstrumentalUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    grupoInstrumentalFormService = TestBed.inject(GrupoInstrumentalFormService);
    grupoInstrumentalService = TestBed.inject(GrupoInstrumentalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const grupoInstrumental: IGrupoInstrumental = { id: 31278 };

      activatedRoute.data = of({ grupoInstrumental });
      comp.ngOnInit();

      expect(comp.grupoInstrumental).toEqual(grupoInstrumental);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGrupoInstrumental>();
      const grupoInstrumental = { id: 30752 };
      vi.spyOn(grupoInstrumentalFormService, 'getGrupoInstrumental').mockReturnValue(grupoInstrumental);
      vi.spyOn(grupoInstrumentalService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grupoInstrumental });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(grupoInstrumental);
      saveSubject.complete();

      // THEN
      expect(grupoInstrumentalFormService.getGrupoInstrumental).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(grupoInstrumentalService.update).toHaveBeenCalledWith(expect.objectContaining(grupoInstrumental));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IGrupoInstrumental>();
      const grupoInstrumental = { id: 30752 };
      vi.spyOn(grupoInstrumentalFormService, 'getGrupoInstrumental').mockReturnValue({ id: null });
      vi.spyOn(grupoInstrumentalService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grupoInstrumental: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(grupoInstrumental);
      saveSubject.complete();

      // THEN
      expect(grupoInstrumentalFormService.getGrupoInstrumental).toHaveBeenCalled();
      expect(grupoInstrumentalService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IGrupoInstrumental>();
      const grupoInstrumental = { id: 30752 };
      vi.spyOn(grupoInstrumentalService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grupoInstrumental });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(grupoInstrumentalService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
