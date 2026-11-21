import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IRecuperacionPassword } from '../recuperacion-password.model';
import { RecuperacionPasswordService } from '../service/recuperacion-password.service';

const recuperacionPasswordResolve = (route: ActivatedRouteSnapshot): Observable<null | IRecuperacionPassword> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(RecuperacionPasswordService);
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

export default recuperacionPasswordResolve;
