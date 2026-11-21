import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { GrupoInstrumentalDetail } from './grupo-instrumental-detail';

describe('GrupoInstrumental Management Detail Component', () => {
  let comp: GrupoInstrumentalDetail;
  let fixture: ComponentFixture<GrupoInstrumentalDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./grupo-instrumental-detail').then(m => m.GrupoInstrumentalDetail),
              resolve: { grupoInstrumental: () => of({ id: 30752 }) },
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
    fixture = TestBed.createComponent(GrupoInstrumentalDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load grupoInstrumental on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GrupoInstrumentalDetail);

      // THEN
      expect(instance.grupoInstrumental()).toEqual(expect.objectContaining({ id: 30752 }));
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
