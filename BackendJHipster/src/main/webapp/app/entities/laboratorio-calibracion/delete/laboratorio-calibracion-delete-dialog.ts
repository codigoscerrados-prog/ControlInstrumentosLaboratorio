import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ILaboratorioCalibracion } from '../laboratorio-calibracion.model';
import { LaboratorioCalibracionService } from '../service/laboratorio-calibracion.service';

@Component({
  templateUrl: './laboratorio-calibracion-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class LaboratorioCalibracionDeleteDialog {
  laboratorioCalibracion?: ILaboratorioCalibracion;

  protected readonly laboratorioCalibracionService = inject(LaboratorioCalibracionService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.laboratorioCalibracionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
