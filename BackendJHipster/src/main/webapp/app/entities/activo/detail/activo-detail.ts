import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IActivo } from '../activo.model';

@Component({
  selector: 'jhi-activo-detail',
  templateUrl: './activo-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class ActivoDetail {
  readonly activo = input<IActivo | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
