import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IAlumno, NewAlumno } from '../alumno.model';

export type PartialUpdateAlumno = Partial<IAlumno> & Pick<IAlumno, 'id'>;

type RestOf<T extends IAlumno | NewAlumno> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestAlumno = RestOf<IAlumno>;

export type NewRestAlumno = RestOf<NewAlumno>;

export type PartialUpdateRestAlumno = RestOf<PartialUpdateAlumno>;

@Service()
export class AlumnosService {
  readonly alumnosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly alumnosResource = httpResource<RestAlumno[]>(() => {
    const params = this.alumnosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of alumno that have been fetched. It is updated when the alumnosResource emits a new value.
   * In case of error while fetching the alumnos, the signal is set to an empty array.
   */
  readonly alumnos = computed(() =>
    (this.alumnosResource.hasValue() ? this.alumnosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/alumnos`;

  protected convertValueFromServer(restAlumno: RestAlumno): IAlumno {
    return {
      ...restAlumno,
      fechaCreacion: restAlumno.fechaCreacion ? dayjs(restAlumno.fechaCreacion) : undefined,
      fechaModificacion: restAlumno.fechaModificacion ? dayjs(restAlumno.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class AlumnoService extends AlumnosService {
  protected readonly http = inject(HttpClient);

  create(alumno: NewAlumno): Observable<IAlumno> {
    const copy = this.convertValueFromClient(alumno);
    return this.http.post<RestAlumno>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alumno: IAlumno): Observable<IAlumno> {
    const copy = this.convertValueFromClient(alumno);
    return this.http
      .put<RestAlumno>(`${this.resourceUrl}/${encodeURIComponent(this.getAlumnoIdentifier(alumno))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alumno: PartialUpdateAlumno): Observable<IAlumno> {
    const copy = this.convertValueFromClient(alumno);
    return this.http
      .patch<RestAlumno>(`${this.resourceUrl}/${encodeURIComponent(this.getAlumnoIdentifier(alumno))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IAlumno> {
    return this.http.get<RestAlumno>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IAlumno[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlumno[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getAlumnoIdentifier(alumno: Pick<IAlumno, 'id'>): number {
    return alumno.id;
  }

  compareAlumno(o1: Pick<IAlumno, 'id'> | null, o2: Pick<IAlumno, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlumnoIdentifier(o1) === this.getAlumnoIdentifier(o2) : o1 === o2;
  }

  addAlumnoToCollectionIfMissing<Type extends Pick<IAlumno, 'id'>>(
    alumnoCollection: Type[],
    ...alumnosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alumnos: Type[] = alumnosToCheck.filter(alumnoItem => alumnoItem !== null && alumnoItem !== undefined);
    if (alumnos.length > 0) {
      const alumnoCollectionIdentifiers = alumnoCollection.map(alumnoItem => this.getAlumnoIdentifier(alumnoItem));
      const alumnosToAdd = alumnos.filter(alumnoItem => {
        const alumnoIdentifier = this.getAlumnoIdentifier(alumnoItem);
        if (alumnoCollectionIdentifiers.includes(alumnoIdentifier)) {
          return false;
        }
        alumnoCollectionIdentifiers.push(alumnoIdentifier);
        return true;
      });
      return [...alumnosToAdd, ...alumnoCollection];
    }
    return alumnoCollection;
  }

  protected convertValueFromClient<T extends IAlumno | NewAlumno | PartialUpdateAlumno>(alumno: T): RestOf<T> {
    return {
      ...alumno,
      fechaCreacion: alumno.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: alumno.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestAlumno): IAlumno {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestAlumno[]): IAlumno[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
