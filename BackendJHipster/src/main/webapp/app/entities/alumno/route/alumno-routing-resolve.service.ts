import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IAlumno } from '../alumno.model';
import { AlumnoService } from '../service/alumno.service';

const alumnoResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlumno> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(AlumnoService);
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

export default alumnoResolve;
