import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ICursos } from '../cursos.model';

@Component({
  selector: 'jhi-cursos-detail',
  templateUrl: './cursos-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatetimePipe],
})
export class CursosDetail {
  readonly cursos = input<ICursos | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
