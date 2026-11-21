import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAlumno } from '../alumno.model';
import { AlumnoService } from '../service/alumno.service';

import { AlumnoFormService } from './alumno-form.service';
import { AlumnoUpdate } from './alumno-update';

describe('Alumno Management Update Component', () => {
  let comp: AlumnoUpdate;
  let fixture: ComponentFixture<AlumnoUpdate>;
  let activatedRoute: ActivatedRoute;
  let alumnoFormService: AlumnoFormService;
  let alumnoService: AlumnoService;

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

    fixture = TestBed.createComponent(AlumnoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alumnoFormService = TestBed.inject(AlumnoFormService);
    alumnoService = TestBed.inject(AlumnoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const alumno: IAlumno = { id: 12601 };

      activatedRoute.data = of({ alumno });
      comp.ngOnInit();

      expect(comp.alumno).toEqual(alumno);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAlumno>();
      const alumno = { id: 25216 };
      vi.spyOn(alumnoFormService, 'getAlumno').mockReturnValue(alumno);
      vi.spyOn(alumnoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alumno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(alumno);
      saveSubject.complete();

      // THEN
      expect(alumnoFormService.getAlumno).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alumnoService.update).toHaveBeenCalledWith(expect.objectContaining(alumno));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAlumno>();
      const alumno = { id: 25216 };
      vi.spyOn(alumnoFormService, 'getAlumno').mockReturnValue({ id: null });
      vi.spyOn(alumnoService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alumno: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(alumno);
      saveSubject.complete();

      // THEN
      expect(alumnoFormService.getAlumno).toHaveBeenCalled();
      expect(alumnoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAlumno>();
      const alumno = { id: 25216 };
      vi.spyOn(alumnoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alumno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alumnoService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
