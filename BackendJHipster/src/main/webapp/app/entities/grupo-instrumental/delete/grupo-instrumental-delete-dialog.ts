import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IGrupoInstrumental } from '../grupo-instrumental.model';
import { GrupoInstrumentalService } from '../service/grupo-instrumental.service';

@Component({
  templateUrl: './grupo-instrumental-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class GrupoInstrumentalDeleteDialog {
  grupoInstrumental?: IGrupoInstrumental;

  protected readonly grupoInstrumentalService = inject(GrupoInstrumentalService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.grupoInstrumentalService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
