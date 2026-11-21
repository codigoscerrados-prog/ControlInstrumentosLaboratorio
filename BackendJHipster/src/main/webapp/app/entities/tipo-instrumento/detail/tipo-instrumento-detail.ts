import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ITipoInstrumento } from '../tipo-instrumento.model';

@Component({
  selector: 'jhi-tipo-instrumento-detail',
  templateUrl: './tipo-instrumento-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class TipoInstrumentoDetail {
  readonly tipoInstrumento = input<ITipoInstrumento | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
