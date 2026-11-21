import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IAlumno } from '../alumno.model';

@Component({
  selector: 'jhi-alumno-detail',
  templateUrl: './alumno-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class AlumnoDetail {
  readonly alumno = input<IAlumno | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
