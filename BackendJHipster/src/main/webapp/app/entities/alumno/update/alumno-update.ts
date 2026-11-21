import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IAlumno } from '../alumno.model';
import { AlumnoService } from '../service/alumno.service';

import { AlumnoFormGroup, AlumnoFormService } from './alumno-form.service';

@Component({
  selector: 'jhi-alumno-update',
  templateUrl: './alumno-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class AlumnoUpdate implements OnInit {
  readonly isSaving = signal(false);
  alumno: IAlumno | null = null;

  protected alumnoService = inject(AlumnoService);
  protected alumnoFormService = inject(AlumnoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlumnoFormGroup = this.alumnoFormService.createAlumnoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alumno }) => {
      this.alumno = alumno;
      if (alumno) {
        this.updateForm(alumno);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const alumno = this.alumnoFormService.getAlumno(this.editForm);
    if (alumno.id === null) {
      this.subscribeToSaveResponse(this.alumnoService.create(alumno));
    } else {
      this.subscribeToSaveResponse(this.alumnoService.update(alumno));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAlumno | null>): void {
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

  protected updateForm(alumno: IAlumno): void {
    this.alumno = alumno;
    this.alumnoFormService.resetForm(this.editForm, alumno);
  }
}
