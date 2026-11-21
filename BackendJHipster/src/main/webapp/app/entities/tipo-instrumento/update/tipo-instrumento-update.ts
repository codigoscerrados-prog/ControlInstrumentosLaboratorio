import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TipoInstrumentoService } from '../service/tipo-instrumento.service';
import { ITipoInstrumento } from '../tipo-instrumento.model';

import { TipoInstrumentoFormGroup, TipoInstrumentoFormService } from './tipo-instrumento-form.service';

@Component({
  selector: 'jhi-tipo-instrumento-update',
  templateUrl: './tipo-instrumento-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TipoInstrumentoUpdate implements OnInit {
  readonly isSaving = signal(false);
  tipoInstrumento: ITipoInstrumento | null = null;

  protected tipoInstrumentoService = inject(TipoInstrumentoService);
  protected tipoInstrumentoFormService = inject(TipoInstrumentoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TipoInstrumentoFormGroup = this.tipoInstrumentoFormService.createTipoInstrumentoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoInstrumento }) => {
      this.tipoInstrumento = tipoInstrumento;
      if (tipoInstrumento) {
        this.updateForm(tipoInstrumento);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const tipoInstrumento = this.tipoInstrumentoFormService.getTipoInstrumento(this.editForm);
    if (tipoInstrumento.id === null) {
      this.subscribeToSaveResponse(this.tipoInstrumentoService.create(tipoInstrumento));
    } else {
      this.subscribeToSaveResponse(this.tipoInstrumentoService.update(tipoInstrumento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITipoInstrumento | null>): void {
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

  protected updateForm(tipoInstrumento: ITipoInstrumento): void {
    this.tipoInstrumento = tipoInstrumento;
    this.tipoInstrumentoFormService.resetForm(this.editForm, tipoInstrumento);
  }
}
