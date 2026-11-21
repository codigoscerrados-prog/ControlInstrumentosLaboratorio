import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { TipoInstrumentoDetail } from './tipo-instrumento-detail';

describe('TipoInstrumento Management Detail Component', () => {
  let comp: TipoInstrumentoDetail;
  let fixture: ComponentFixture<TipoInstrumentoDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tipo-instrumento-detail').then(m => m.TipoInstrumentoDetail),
              resolve: { tipoInstrumento: () => of({ id: 2947 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TipoInstrumentoDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load tipoInstrumento on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TipoInstrumentoDetail);

      // THEN
      expect(instance.tipoInstrumento()).toEqual(expect.objectContaining({ id: 2947 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
