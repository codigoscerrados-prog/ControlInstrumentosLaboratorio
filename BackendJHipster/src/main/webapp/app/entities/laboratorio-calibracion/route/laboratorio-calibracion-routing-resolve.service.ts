import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ILaboratorioCalibracion } from '../laboratorio-calibracion.model';
import { LaboratorioCalibracionService } from '../service/laboratorio-calibracion.service';

const laboratorioCalibracionResolve = (route: ActivatedRouteSnapshot): Observable<null | ILaboratorioCalibracion> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(LaboratorioCalibracionService);
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

export default laboratorioCalibracionResolve;
