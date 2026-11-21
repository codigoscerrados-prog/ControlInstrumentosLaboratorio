import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICursos } from '../cursos.model';
import { CursosService } from '../service/cursos.service';

import { CursosFormGroup, CursosFormService } from './cursos-form.service';

@Component({
  selector: 'jhi-cursos-update',
  templateUrl: './cursos-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CursosUpdate implements OnInit {
  readonly isSaving = signal(false);
  cursos: ICursos | null = null;

  protected cursosService = inject(CursosService);
  protected cursosFormService = inject(CursosFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CursosFormGroup = this.cursosFormService.createCursosFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cursos }) => {
      this.cursos = cursos;
      if (cursos) {
        this.updateForm(cursos);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const cursos = this.cursosFormService.getCursos(this.editForm);
    if (cursos.id === null) {
      this.subscribeToSaveResponse(this.cursosService.create(cursos));
    } else {
      this.subscribeToSaveResponse(this.cursosService.update(cursos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICursos | null>): void {
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

  protected updateForm(cursos: ICursos): void {
    this.cursos = cursos;
    this.cursosFormService.resetForm(this.editForm, cursos);
  }
}
