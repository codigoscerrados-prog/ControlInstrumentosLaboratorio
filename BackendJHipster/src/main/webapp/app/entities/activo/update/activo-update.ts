import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IGrupoInstrumental } from 'app/entities/grupo-instrumental/grupo-instrumental.model';
import { GrupoInstrumentalService } from 'app/entities/grupo-instrumental/service/grupo-instrumental.service';
import { TipoInstrumentoService } from 'app/entities/tipo-instrumento/service/tipo-instrumento.service';
import { ITipoInstrumento } from 'app/entities/tipo-instrumento/tipo-instrumento.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IActivo } from '../activo.model';
import { ActivoService } from '../service/activo.service';

import { ActivoFormGroup, ActivoFormService } from './activo-form.service';

@Component({
  selector: 'jhi-activo-update',
  templateUrl: './activo-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ActivoUpdate implements OnInit {
  readonly isSaving = signal(false);
  activo: IActivo | null = null;

  grupoInstrumentalsSharedCollection = signal<IGrupoInstrumental[]>([]);
  tipoInstrumentosSharedCollection = signal<ITipoInstrumento[]>([]);

  protected activoService = inject(ActivoService);
  protected activoFormService = inject(ActivoFormService);
  protected grupoInstrumentalService = inject(GrupoInstrumentalService);
  protected tipoInstrumentoService = inject(TipoInstrumentoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ActivoFormGroup = this.activoFormService.createActivoFormGroup();

  compareGrupoInstrumental = (o1: IGrupoInstrumental | null, o2: IGrupoInstrumental | null): boolean =>
    this.grupoInstrumentalService.compareGrupoInstrumental(o1, o2);

  compareTipoInstrumento = (o1: ITipoInstrumento | null, o2: ITipoInstrumento | null): boolean =>
    this.tipoInstrumentoService.compareTipoInstrumento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activo }) => {
      this.activo = activo;
      if (activo) {
        this.updateForm(activo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const activo = this.activoFormService.getActivo(this.editForm);
    if (activo.id === null) {
      this.subscribeToSaveResponse(this.activoService.create(activo));
    } else {
      this.subscribeToSaveResponse(this.activoService.update(activo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IActivo | null>): void {
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

  protected updateForm(activo: IActivo): void {
    this.activo = activo;
    this.activoFormService.resetForm(this.editForm, activo);

    this.grupoInstrumentalsSharedCollection.update(grupoInstrumentals =>
      this.grupoInstrumentalService.addGrupoInstrumentalToCollectionIfMissing<IGrupoInstrumental>(grupoInstrumentals, activo.grupo),
    );
    this.tipoInstrumentosSharedCollection.update(tipoInstrumentos =>
      this.tipoInstrumentoService.addTipoInstrumentoToCollectionIfMissing<ITipoInstrumento>(tipoInstrumentos, activo.tipoInstrumento),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.grupoInstrumentalService
      .query()
      .pipe(map((res: HttpResponse<IGrupoInstrumental[]>) => res.body ?? []))
      .pipe(
        map((grupoInstrumentals: IGrupoInstrumental[]) =>
          this.grupoInstrumentalService.addGrupoInstrumentalToCollectionIfMissing<IGrupoInstrumental>(
            grupoInstrumentals,
            this.activo?.grupo,
          ),
        ),
      )
      .subscribe((grupoInstrumentals: IGrupoInstrumental[]) => this.grupoInstrumentalsSharedCollection.set(grupoInstrumentals));

    this.tipoInstrumentoService
      .query()
      .pipe(map((res: HttpResponse<ITipoInstrumento[]>) => res.body ?? []))
      .pipe(
        map((tipoInstrumentos: ITipoInstrumento[]) =>
          this.tipoInstrumentoService.addTipoInstrumentoToCollectionIfMissing<ITipoInstrumento>(
            tipoInstrumentos,
            this.activo?.tipoInstrumento,
          ),
        ),
      )
      .subscribe((tipoInstrumentos: ITipoInstrumento[]) => this.tipoInstrumentosSharedCollection.set(tipoInstrumentos));
  }
}
