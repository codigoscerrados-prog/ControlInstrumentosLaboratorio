import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { RecuperacionPasswordDetail } from './recuperacion-password-detail';

describe('RecuperacionPassword Management Detail Component', () => {
  let comp: RecuperacionPasswordDetail;
  let fixture: ComponentFixture<RecuperacionPasswordDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./recuperacion-password-detail').then(m => m.RecuperacionPasswordDetail),
              resolve: { recuperacionPassword: () => of({ id: 27296 }) },
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
    fixture = TestBed.createComponent(RecuperacionPasswordDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load recuperacionPassword on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RecuperacionPasswordDetail);

      // THEN
      expect(instance.recuperacionPassword()).toEqual(expect.objectContaining({ id: 27296 }));
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
