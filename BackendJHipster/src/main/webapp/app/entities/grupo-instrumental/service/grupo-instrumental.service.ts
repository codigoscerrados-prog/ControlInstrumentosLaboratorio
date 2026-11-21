import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IGrupoInstrumental, NewGrupoInstrumental } from '../grupo-instrumental.model';

export type PartialUpdateGrupoInstrumental = Partial<IGrupoInstrumental> & Pick<IGrupoInstrumental, 'id'>;

type RestOf<T extends IGrupoInstrumental | NewGrupoInstrumental> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestGrupoInstrumental = RestOf<IGrupoInstrumental>;

export type NewRestGrupoInstrumental = RestOf<NewGrupoInstrumental>;

export type PartialUpdateRestGrupoInstrumental = RestOf<PartialUpdateGrupoInstrumental>;

@Service()
export class GrupoInstrumentalsService {
  readonly grupoInstrumentalsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly grupoInstrumentalsResource = httpResource<RestGrupoInstrumental[]>(() => {
    const params = this.grupoInstrumentalsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of grupoInstrumental that have been fetched. It is updated when the grupoInstrumentalsResource emits a new value.
   * In case of error while fetching the grupoInstrumentals, the signal is set to an empty array.
   */
  readonly grupoInstrumentals = computed(() =>
    (this.grupoInstrumentalsResource.hasValue() ? this.grupoInstrumentalsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/grupo-instrumentals`;

  protected convertValueFromServer(restGrupoInstrumental: RestGrupoInstrumental): IGrupoInstrumental {
    return {
      ...restGrupoInstrumental,
      fechaCreacion: restGrupoInstrumental.fechaCreacion ? dayjs(restGrupoInstrumental.fechaCreacion) : undefined,
      fechaModificacion: restGrupoInstrumental.fechaModificacion ? dayjs(restGrupoInstrumental.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class GrupoInstrumentalService extends GrupoInstrumentalsService {
  protected readonly http = inject(HttpClient);

  create(grupoInstrumental: NewGrupoInstrumental): Observable<IGrupoInstrumental> {
    const copy = this.convertValueFromClient(grupoInstrumental);
    return this.http.post<RestGrupoInstrumental>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(grupoInstrumental: IGrupoInstrumental): Observable<IGrupoInstrumental> {
    const copy = this.convertValueFromClient(grupoInstrumental);
    return this.http
      .put<RestGrupoInstrumental>(`${this.resourceUrl}/${encodeURIComponent(this.getGrupoInstrumentalIdentifier(grupoInstrumental))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(grupoInstrumental: PartialUpdateGrupoInstrumental): Observable<IGrupoInstrumental> {
    const copy = this.convertValueFromClient(grupoInstrumental);
    return this.http
      .patch<RestGrupoInstrumental>(
        `${this.resourceUrl}/${encodeURIComponent(this.getGrupoInstrumentalIdentifier(grupoInstrumental))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IGrupoInstrumental> {
    return this.http
      .get<RestGrupoInstrumental>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IGrupoInstrumental[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestGrupoInstrumental[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getGrupoInstrumentalIdentifier(grupoInstrumental: Pick<IGrupoInstrumental, 'id'>): number {
    return grupoInstrumental.id;
  }

  compareGrupoInstrumental(o1: Pick<IGrupoInstrumental, 'id'> | null, o2: Pick<IGrupoInstrumental, 'id'> | null): boolean {
    return o1 && o2 ? this.getGrupoInstrumentalIdentifier(o1) === this.getGrupoInstrumentalIdentifier(o2) : o1 === o2;
  }

  addGrupoInstrumentalToCollectionIfMissing<Type extends Pick<IGrupoInstrumental, 'id'>>(
    grupoInstrumentalCollection: Type[],
    ...grupoInstrumentalsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const grupoInstrumentals: Type[] = grupoInstrumentalsToCheck.filter(
      grupoInstrumentalItem => grupoInstrumentalItem !== null && grupoInstrumentalItem !== undefined,
    );
    if (grupoInstrumentals.length > 0) {
      const grupoInstrumentalCollectionIdentifiers = grupoInstrumentalCollection.map(grupoInstrumentalItem =>
        this.getGrupoInstrumentalIdentifier(grupoInstrumentalItem),
      );
      const grupoInstrumentalsToAdd = grupoInstrumentals.filter(grupoInstrumentalItem => {
        const grupoInstrumentalIdentifier = this.getGrupoInstrumentalIdentifier(grupoInstrumentalItem);
        if (grupoInstrumentalCollectionIdentifiers.includes(grupoInstrumentalIdentifier)) {
          return false;
        }
        grupoInstrumentalCollectionIdentifiers.push(grupoInstrumentalIdentifier);
        return true;
      });
      return [...grupoInstrumentalsToAdd, ...grupoInstrumentalCollection];
    }
    return grupoInstrumentalCollection;
  }

  protected convertValueFromClient<T extends IGrupoInstrumental | NewGrupoInstrumental | PartialUpdateGrupoInstrumental>(
    grupoInstrumental: T,
  ): RestOf<T> {
    return {
      ...grupoInstrumental,
      fechaCreacion: grupoInstrumental.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: grupoInstrumental.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestGrupoInstrumental): IGrupoInstrumental {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestGrupoInstrumental[]): IGrupoInstrumental[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
