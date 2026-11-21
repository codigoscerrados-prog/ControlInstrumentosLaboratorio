import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IActivo, NewActivo } from '../activo.model';

export type PartialUpdateActivo = Partial<IActivo> & Pick<IActivo, 'id'>;

type RestOf<T extends IActivo | NewActivo> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestActivo = RestOf<IActivo>;

export type NewRestActivo = RestOf<NewActivo>;

export type PartialUpdateRestActivo = RestOf<PartialUpdateActivo>;

@Service()
export class ActivosService {
  readonly activosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly activosResource = httpResource<RestActivo[]>(() => {
    const params = this.activosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of activo that have been fetched. It is updated when the activosResource emits a new value.
   * In case of error while fetching the activos, the signal is set to an empty array.
   */
  readonly activos = computed(() =>
    (this.activosResource.hasValue() ? this.activosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/activos`;

  protected convertValueFromServer(restActivo: RestActivo): IActivo {
    return {
      ...restActivo,
      fechaCreacion: restActivo.fechaCreacion ? dayjs(restActivo.fechaCreacion) : undefined,
      fechaModificacion: restActivo.fechaModificacion ? dayjs(restActivo.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class ActivoService extends ActivosService {
  protected readonly http = inject(HttpClient);

  create(activo: NewActivo): Observable<IActivo> {
    const copy = this.convertValueFromClient(activo);
    return this.http.post<RestActivo>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(activo: IActivo): Observable<IActivo> {
    const copy = this.convertValueFromClient(activo);
    return this.http
      .put<RestActivo>(`${this.resourceUrl}/${encodeURIComponent(this.getActivoIdentifier(activo))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(activo: PartialUpdateActivo): Observable<IActivo> {
    const copy = this.convertValueFromClient(activo);
    return this.http
      .patch<RestActivo>(`${this.resourceUrl}/${encodeURIComponent(this.getActivoIdentifier(activo))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IActivo> {
    return this.http.get<RestActivo>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IActivo[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestActivo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getActivoIdentifier(activo: Pick<IActivo, 'id'>): number {
    return activo.id;
  }

  compareActivo(o1: Pick<IActivo, 'id'> | null, o2: Pick<IActivo, 'id'> | null): boolean {
    return o1 && o2 ? this.getActivoIdentifier(o1) === this.getActivoIdentifier(o2) : o1 === o2;
  }

  addActivoToCollectionIfMissing<Type extends Pick<IActivo, 'id'>>(
    activoCollection: Type[],
    ...activosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const activos: Type[] = activosToCheck.filter(activoItem => activoItem !== null && activoItem !== undefined);
    if (activos.length > 0) {
      const activoCollectionIdentifiers = activoCollection.map(activoItem => this.getActivoIdentifier(activoItem));
      const activosToAdd = activos.filter(activoItem => {
        const activoIdentifier = this.getActivoIdentifier(activoItem);
        if (activoCollectionIdentifiers.includes(activoIdentifier)) {
          return false;
        }
        activoCollectionIdentifiers.push(activoIdentifier);
        return true;
      });
      return [...activosToAdd, ...activoCollection];
    }
    return activoCollection;
  }

  protected convertValueFromClient<T extends IActivo | NewActivo | PartialUpdateActivo>(activo: T): RestOf<T> {
    return {
      ...activo,
      fechaCreacion: activo.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: activo.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestActivo): IActivo {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestActivo[]): IActivo[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
