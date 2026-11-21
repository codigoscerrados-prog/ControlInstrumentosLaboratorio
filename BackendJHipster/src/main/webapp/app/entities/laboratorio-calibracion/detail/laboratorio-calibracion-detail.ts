import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ILaboratorioCalibracion } from '../laboratorio-calibracion.model';

@Component({
  selector: 'jhi-laboratorio-calibracion-detail',
  templateUrl: './laboratorio-calibracion-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class LaboratorioCalibracionDetail {
  readonly laboratorioCalibracion = input<ILaboratorioCalibracion | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
