import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICursos } from '../cursos.model';
import { CursosService } from '../service/cursos.service';

import { CursosFormService } from './cursos-form.service';
import { CursosUpdate } from './cursos-update';

describe('Cursos Management Update Component', () => {
  let comp: CursosUpdate;
  let fixture: ComponentFixture<CursosUpdate>;
  let activatedRoute: ActivatedRoute;
  let cursosFormService: CursosFormService;
  let cursosService: CursosService;

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

    fixture = TestBed.createComponent(CursosUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cursosFormService = TestBed.inject(CursosFormService);
    cursosService = TestBed.inject(CursosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const cursos: ICursos = { id: 27258 };

      activatedRoute.data = of({ cursos });
      comp.ngOnInit();

      expect(comp.cursos).toEqual(cursos);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICursos>();
      const cursos = { id: 8714 };
      vi.spyOn(cursosFormService, 'getCursos').mockReturnValue(cursos);
      vi.spyOn(cursosService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cursos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cursos);
      saveSubject.complete();

      // THEN
      expect(cursosFormService.getCursos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cursosService.update).toHaveBeenCalledWith(expect.objectContaining(cursos));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICursos>();
      const cursos = { id: 8714 };
      vi.spyOn(cursosFormService, 'getCursos').mockReturnValue({ id: null });
      vi.spyOn(cursosService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cursos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cursos);
      saveSubject.complete();

      // THEN
      expect(cursosFormService.getCursos).toHaveBeenCalled();
      expect(cursosService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICursos>();
      const cursos = { id: 8714 };
      vi.spyOn(cursosService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cursos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cursosService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
