import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IRecuperacionPassword } from '../recuperacion-password.model';
import { RecuperacionPasswordService } from '../service/recuperacion-password.service';

import { RecuperacionPasswordFormGroup, RecuperacionPasswordFormService } from './recuperacion-password-form.service';

@Component({
  selector: 'jhi-recuperacion-password-update',
  templateUrl: './recuperacion-password-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class RecuperacionPasswordUpdate implements OnInit {
  readonly isSaving = signal(false);
  recuperacionPassword: IRecuperacionPassword | null = null;

  usuariosSharedCollection = signal<IUsuario[]>([]);

  protected recuperacionPasswordService = inject(RecuperacionPasswordService);
  protected recuperacionPasswordFormService = inject(RecuperacionPasswordFormService);
  protected usuarioService = inject(UsuarioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecuperacionPasswordFormGroup = this.recuperacionPasswordFormService.createRecuperacionPasswordFormGroup();

  compareUsuario = (o1: IUsuario | null, o2: IUsuario | null): boolean => this.usuarioService.compareUsuario(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recuperacionPassword }) => {
      this.recuperacionPassword = recuperacionPassword;
      if (recuperacionPassword) {
        this.updateForm(recuperacionPassword);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const recuperacionPassword = this.recuperacionPasswordFormService.getRecuperacionPassword(this.editForm);
    if (recuperacionPassword.id === null) {
      this.subscribeToSaveResponse(this.recuperacionPasswordService.create(recuperacionPassword));
    } else {
      this.subscribeToSaveResponse(this.recuperacionPasswordService.update(recuperacionPassword));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IRecuperacionPassword | null>): void {
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

  protected updateForm(recuperacionPassword: IRecuperacionPassword): void {
    this.recuperacionPassword = recuperacionPassword;
    this.recuperacionPasswordFormService.resetForm(this.editForm, recuperacionPassword);

    this.usuariosSharedCollection.update(usuarios =>
      this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(usuarios, recuperacionPassword.usuario),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(usuarios, this.recuperacionPassword?.usuario),
        ),
      )
      .subscribe((usuarios: IUsuario[]) => this.usuariosSharedCollection.set(usuarios));
  }
}
