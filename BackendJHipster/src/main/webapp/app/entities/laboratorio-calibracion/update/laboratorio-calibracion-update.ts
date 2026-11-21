import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ILaboratorioCalibracion } from '../laboratorio-calibracion.model';
import { LaboratorioCalibracionService } from '../service/laboratorio-calibracion.service';

import { LaboratorioCalibracionFormGroup, LaboratorioCalibracionFormService } from './laboratorio-calibracion-form.service';

@Component({
  selector: 'jhi-laboratorio-calibracion-update',
  templateUrl: './laboratorio-calibracion-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class LaboratorioCalibracionUpdate implements OnInit {
  readonly isSaving = signal(false);
  laboratorioCalibracion: ILaboratorioCalibracion | null = null;

  protected laboratorioCalibracionService = inject(LaboratorioCalibracionService);
  protected laboratorioCalibracionFormService = inject(LaboratorioCalibracionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LaboratorioCalibracionFormGroup = this.laboratorioCalibracionFormService.createLaboratorioCalibracionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ laboratorioCalibracion }) => {
      this.laboratorioCalibracion = laboratorioCalibracion;
      if (laboratorioCalibracion) {
        this.updateForm(laboratorioCalibracion);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const laboratorioCalibracion = this.laboratorioCalibracionFormService.getLaboratorioCalibracion(this.editForm);
    if (laboratorioCalibracion.id === null) {
      this.subscribeToSaveResponse(this.laboratorioCalibracionService.create(laboratorioCalibracion));
    } else {
      this.subscribeToSaveResponse(this.laboratorioCalibracionService.update(laboratorioCalibracion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ILaboratorioCalibracion | null>): void {
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

  protected updateForm(laboratorioCalibracion: ILaboratorioCalibracion): void {
    this.laboratorioCalibracion = laboratorioCalibracion;
    this.laboratorioCalibracionFormService.resetForm(this.editForm, laboratorioCalibracion);
  }
}
