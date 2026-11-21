import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IRecuperacionPassword } from '../recuperacion-password.model';
import { RecuperacionPasswordService } from '../service/recuperacion-password.service';

import { RecuperacionPasswordFormService } from './recuperacion-password-form.service';
import { RecuperacionPasswordUpdate } from './recuperacion-password-update';

describe('RecuperacionPassword Management Update Component', () => {
  let comp: RecuperacionPasswordUpdate;
  let fixture: ComponentFixture<RecuperacionPasswordUpdate>;
  let activatedRoute: ActivatedRoute;
  let recuperacionPasswordFormService: RecuperacionPasswordFormService;
  let recuperacionPasswordService: RecuperacionPasswordService;
  let usuarioService: UsuarioService;

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

    fixture = TestBed.createComponent(RecuperacionPasswordUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recuperacionPasswordFormService = TestBed.inject(RecuperacionPasswordFormService);
    recuperacionPasswordService = TestBed.inject(RecuperacionPasswordService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Usuario query and add missing value', () => {
      const recuperacionPassword: IRecuperacionPassword = { id: 29135 };
      const usuario: IUsuario = { id: 544 };
      recuperacionPassword.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 544 }];
      vi.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      vi.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recuperacionPassword });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(
        usuarioCollection,
        ...additionalUsuarios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usuariosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const recuperacionPassword: IRecuperacionPassword = { id: 29135 };
      const usuario: IUsuario = { id: 544 };
      recuperacionPassword.usuario = usuario;

      activatedRoute.data = of({ recuperacionPassword });
      comp.ngOnInit();

      expect(comp.usuariosSharedCollection()).toContainEqual(usuario);
      expect(comp.recuperacionPassword).toEqual(recuperacionPassword);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRecuperacionPassword>();
      const recuperacionPassword = { id: 27296 };
      vi.spyOn(recuperacionPasswordFormService, 'getRecuperacionPassword').mockReturnValue(recuperacionPassword);
      vi.spyOn(recuperacionPasswordService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recuperacionPassword });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(recuperacionPassword);
      saveSubject.complete();

      // THEN
      expect(recuperacionPasswordFormService.getRecuperacionPassword).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recuperacionPasswordService.update).toHaveBeenCalledWith(expect.objectContaining(recuperacionPassword));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRecuperacionPassword>();
      const recuperacionPassword = { id: 27296 };
      vi.spyOn(recuperacionPasswordFormService, 'getRecuperacionPassword').mockReturnValue({ id: null });
      vi.spyOn(recuperacionPasswordService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recuperacionPassword: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(recuperacionPassword);
      saveSubject.complete();

      // THEN
      expect(recuperacionPasswordFormService.getRecuperacionPassword).toHaveBeenCalled();
      expect(recuperacionPasswordService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IRecuperacionPassword>();
      const recuperacionPassword = { id: 27296 };
      vi.spyOn(recuperacionPasswordService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recuperacionPassword });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recuperacionPasswordService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUsuario', () => {
      it('should forward to usuarioService', () => {
        const entity = { id: 544 };
        const entity2 = { id: 13162 };
        vi.spyOn(usuarioService, 'compareUsuario');
        comp.compareUsuario(entity, entity2);
        expect(usuarioService.compareUsuario).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
