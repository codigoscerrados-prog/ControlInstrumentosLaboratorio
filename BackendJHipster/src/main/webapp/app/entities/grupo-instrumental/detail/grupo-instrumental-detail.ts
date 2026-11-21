import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IGrupoInstrumental } from '../grupo-instrumental.model';

@Component({
  selector: 'jhi-grupo-instrumental-detail',
  templateUrl: './grupo-instrumental-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class GrupoInstrumentalDetail {
  readonly grupoInstrumental = input<IGrupoInstrumental | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
