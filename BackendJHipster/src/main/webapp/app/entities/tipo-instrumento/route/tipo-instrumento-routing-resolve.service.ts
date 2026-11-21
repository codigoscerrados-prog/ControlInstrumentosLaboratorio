import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TipoInstrumentoService } from '../service/tipo-instrumento.service';
import { ITipoInstrumento } from '../tipo-instrumento.model';

const tipoInstrumentoResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoInstrumento> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TipoInstrumentoService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default tipoInstrumentoResolve;
