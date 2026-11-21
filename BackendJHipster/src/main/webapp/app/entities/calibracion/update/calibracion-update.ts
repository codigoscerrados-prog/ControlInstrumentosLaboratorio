import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { IActivo } from 'app/entities/activo/activo.model';
import { ActivoService } from 'app/entities/activo/service/activo.service';
import { ILaboratorioCalibracion } from 'app/entities/laboratorio-calibracion/laboratorio-calibracion.model';
import { LaboratorioCalibracionService } from 'app/entities/laboratorio-calibracion/service/laboratorio-calibracion.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICalibracion } from '../calibracion.model';
import { CalibracionService } from '../service/calibracion.service';

import { CalibracionFormGroup, CalibracionFormService } from './calibracion-form.service';

@Component({
  selector: 'jhi-calibracion-update',
  templateUrl: './calibracion-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class CalibracionUpdate implements OnInit {
  readonly isSaving = signal(false);
  calibracion: ICalibracion | null = null;

  activosSharedCollection = signal<IActivo[]>([]);
  laboratorioCalibracionsSharedCollection = signal<ILaboratorioCalibracion[]>([]);

  protected calibracionService = inject(CalibracionService);
  protected calibracionFormService = inject(CalibracionFormService);
  protected activoService = inject(ActivoService);
  protected laboratorioCalibracionService = inject(LaboratorioCalibracionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CalibracionFormGroup = this.calibracionFormService.createCalibracionFormGroup();

  compareActivo = (o1: IActivo | null, o2: IActivo | null): boolean => this.activoService.compareActivo(o1, o2);

  compareLaboratorioCalibracion = (o1: ILaboratorioCalibracion | null, o2: ILaboratorioCalibracion | null): boolean =>
    this.laboratorioCalibracionService.compareLaboratorioCalibracion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calibracion }) => {
      this.calibracion = calibracion;
      if (calibracion) {
        this.updateForm(calibracion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const calibracion = this.calibracionFormService.getCalibracion(this.editForm);
    if (calibracion.id === null) {
      this.subscribeToSaveResponse(this.calibracionService.create(calibracion));
    } else {
      this.subscribeToSaveResponse(this.calibracionService.update(calibracion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICalibracion | null>): void {
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

  protected updateForm(calibracion: ICalibracion): void {
    this.calibracion = calibracion;
    this.calibracionFormService.resetForm(this.editForm, calibracion);

    this.activosSharedCollection.update(activos => this.activoService.addActivoToCollectionIfMissing<IActivo>(activos, calibracion.activo));
    this.laboratorioCalibracionsSharedCollection.update(laboratorioCalibracions =>
      this.laboratorioCalibracionService.addLaboratorioCalibracionToCollectionIfMissing<ILaboratorioCalibracion>(
        laboratorioCalibracions,
        calibracion.laboratorio,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.activoService
      .query()
      .pipe(map((res: HttpResponse<IActivo[]>) => res.body ?? []))
      .pipe(map((activos: IActivo[]) => this.activoService.addActivoToCollectionIfMissing<IActivo>(activos, this.calibracion?.activo)))
      .subscribe((activos: IActivo[]) => this.activosSharedCollection.set(activos));

    this.laboratorioCalibracionService
      .query()
      .pipe(map((res: HttpResponse<ILaboratorioCalibracion[]>) => res.body ?? []))
      .pipe(
        map((laboratorioCalibracions: ILaboratorioCalibracion[]) =>
          this.laboratorioCalibracionService.addLaboratorioCalibracionToCollectionIfMissing<ILaboratorioCalibracion>(
            laboratorioCalibracions,
            this.calibracion?.laboratorio,
          ),
        ),
      )
      .subscribe((laboratorioCalibracions: ILaboratorioCalibracion[]) =>
        this.laboratorioCalibracionsSharedCollection.set(laboratorioCalibracions),
      );
  }
}
