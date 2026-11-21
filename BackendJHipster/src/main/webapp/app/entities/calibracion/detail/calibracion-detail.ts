import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICalibracion } from '../calibracion.model';

@Component({
  selector: 'jhi-calibracion-detail',
  templateUrl: './calibracion-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CalibracionDetail {
  readonly calibracion = input<ICalibracion | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
