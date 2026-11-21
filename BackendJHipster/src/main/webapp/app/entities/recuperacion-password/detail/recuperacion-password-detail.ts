import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IRecuperacionPassword } from '../recuperacion-password.model';

@Component({
  selector: 'jhi-recuperacion-password-detail',
  templateUrl: './recuperacion-password-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class RecuperacionPasswordDetail {
  readonly recuperacionPassword = input<IRecuperacionPassword | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
