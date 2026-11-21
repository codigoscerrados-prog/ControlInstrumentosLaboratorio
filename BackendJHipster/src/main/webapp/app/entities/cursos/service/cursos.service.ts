import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICursos, NewCursos } from '../cursos.model';

export type PartialUpdateCursos = Partial<ICursos> & Pick<ICursos, 'id'>;

type RestOf<T extends ICursos | NewCursos> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestCursos = RestOf<ICursos>;

export type NewRestCursos = RestOf<NewCursos>;

export type PartialUpdateRestCursos = RestOf<PartialUpdateCursos>;

@Service()
export class CursosesService {
  readonly cursosesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly cursosesResource = httpResource<RestCursos[]>(() => {
    const params = this.cursosesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of cursos that have been fetched. It is updated when the cursosesResource emits a new value.
   * In case of error while fetching the cursoses, the signal is set to an empty array.
   */
  readonly cursoses = computed(() =>
    (this.cursosesResource.hasValue() ? this.cursosesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/cursos`;

  protected convertValueFromServer(restCursos: RestCursos): ICursos {
    return {
      ...restCursos,
      fechaCreacion: restCursos.fechaCreacion ? dayjs(restCursos.fechaCreacion) : undefined,
      fechaModificacion: restCursos.fechaModificacion ? dayjs(restCursos.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class CursosService extends CursosesService {
  protected readonly http = inject(HttpClient);

  create(cursos: NewCursos): Observable<ICursos> {
    const copy = this.convertValueFromClient(cursos);
    return this.http.post<RestCursos>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cursos: ICursos): Observable<ICursos> {
    const copy = this.convertValueFromClient(cursos);
    return this.http
      .put<RestCursos>(`${this.resourceUrl}/${encodeURIComponent(this.getCursosIdentifier(cursos))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cursos: PartialUpdateCursos): Observable<ICursos> {
    const copy = this.convertValueFromClient(cursos);
    return this.http
      .patch<RestCursos>(`${this.resourceUrl}/${encodeURIComponent(this.getCursosIdentifier(cursos))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICursos> {
    return this.http.get<RestCursos>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICursos[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCursos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCursosIdentifier(cursos: Pick<ICursos, 'id'>): number {
    return cursos.id;
  }

  compareCursos(o1: Pick<ICursos, 'id'> | null, o2: Pick<ICursos, 'id'> | null): boolean {
    return o1 && o2 ? this.getCursosIdentifier(o1) === this.getCursosIdentifier(o2) : o1 === o2;
  }

  addCursosToCollectionIfMissing<Type extends Pick<ICursos, 'id'>>(
    cursosCollection: Type[],
    ...cursosesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cursoses: Type[] = cursosesToCheck.filter(cursosItem => cursosItem !== null && cursosItem !== undefined);
    if (cursoses.length > 0) {
      const cursosCollectionIdentifiers = cursosCollection.map(cursosItem => this.getCursosIdentifier(cursosItem));
      const cursosesToAdd = cursoses.filter(cursosItem => {
        const cursosIdentifier = this.getCursosIdentifier(cursosItem);
        if (cursosCollectionIdentifiers.includes(cursosIdentifier)) {
          return false;
        }
        cursosCollectionIdentifiers.push(cursosIdentifier);
        return true;
      });
      return [...cursosesToAdd, ...cursosCollection];
    }
    return cursosCollection;
  }

  protected convertValueFromClient<T extends ICursos | NewCursos | PartialUpdateCursos>(cursos: T): RestOf<T> {
    return {
      ...cursos,
      fechaCreacion: cursos.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: cursos.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCursos): ICursos {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCursos[]): ICursos[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
