import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGrupoInstrumental } from '../grupo-instrumental.model';
import { GrupoInstrumentalService } from '../service/grupo-instrumental.service';

import { GrupoInstrumentalFormGroup, GrupoInstrumentalFormService } from './grupo-instrumental-form.service';

@Component({
  selector: 'jhi-grupo-instrumental-update',
  templateUrl: './grupo-instrumental-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class GrupoInstrumentalUpdate implements OnInit {
  readonly isSaving = signal(false);
  grupoInstrumental: IGrupoInstrumental | null = null;

  protected grupoInstrumentalService = inject(GrupoInstrumentalService);
  protected grupoInstrumentalFormService = inject(GrupoInstrumentalFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GrupoInstrumentalFormGroup = this.grupoInstrumentalFormService.createGrupoInstrumentalFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grupoInstrumental }) => {
      this.grupoInstrumental = grupoInstrumental;
      if (grupoInstrumental) {
        this.updateForm(grupoInstrumental);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const grupoInstrumental = this.grupoInstrumentalFormService.getGrupoInstrumental(this.editForm);
    if (grupoInstrumental.id === null) {
      this.subscribeToSaveResponse(this.grupoInstrumentalService.create(grupoInstrumental));
    } else {
      this.subscribeToSaveResponse(this.grupoInstrumentalService.update(grupoInstrumental));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IGrupoInstrumental | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(grupoInstrumental: IGrupoInstrumental): void {
    this.grupoInstrumental = grupoInstrumental;
    this.grupoInstrumentalFormService.resetForm(this.editForm, grupoInstrumental);
  }
}
