import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TipoInstrumentoService } from '../service/tipo-instrumento.service';
import { ITipoInstrumento } from '../tipo-instrumento.model';

@Component({
  templateUrl: './tipo-instrumento-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class TipoInstrumentoDeleteDialog {
  tipoInstrumento?: ITipoInstrumento;

  protected readonly tipoInstrumentoService = inject(TipoInstrumentoService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoInstrumentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
