import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITipoInstrumento, NewTipoInstrumento } from '../tipo-instrumento.model';

export type PartialUpdateTipoInstrumento = Partial<ITipoInstrumento> & Pick<ITipoInstrumento, 'id'>;

type RestOf<T extends ITipoInstrumento | NewTipoInstrumento> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestTipoInstrumento = RestOf<ITipoInstrumento>;

export type NewRestTipoInstrumento = RestOf<NewTipoInstrumento>;

export type PartialUpdateRestTipoInstrumento = RestOf<PartialUpdateTipoInstrumento>;

@Service()
export class TipoInstrumentosService {
  readonly tipoInstrumentosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly tipoInstrumentosResource = httpResource<RestTipoInstrumento[]>(() => {
    const params = this.tipoInstrumentosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of tipoInstrumento that have been fetched. It is updated when the tipoInstrumentosResource emits a new value.
   * In case of error while fetching the tipoInstrumentos, the signal is set to an empty array.
   */
  readonly tipoInstrumentos = computed(() =>
    (this.tipoInstrumentosResource.hasValue() ? this.tipoInstrumentosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/tipo-instrumentos`;

  protected convertValueFromServer(restTipoInstrumento: RestTipoInstrumento): ITipoInstrumento {
    return {
      ...restTipoInstrumento,
      fechaCreacion: restTipoInstrumento.fechaCreacion ? dayjs(restTipoInstrumento.fechaCreacion) : undefined,
      fechaModificacion: restTipoInstrumento.fechaModificacion ? dayjs(restTipoInstrumento.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class TipoInstrumentoService extends TipoInstrumentosService {
  protected readonly http = inject(HttpClient);

  create(tipoInstrumento: NewTipoInstrumento): Observable<ITipoInstrumento> {
    const copy = this.convertValueFromClient(tipoInstrumento);
    return this.http.post<RestTipoInstrumento>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tipoInstrumento: ITipoInstrumento): Observable<ITipoInstrumento> {
    const copy = this.convertValueFromClient(tipoInstrumento);
    return this.http
      .put<RestTipoInstrumento>(`${this.resourceUrl}/${encodeURIComponent(this.getTipoInstrumentoIdentifier(tipoInstrumento))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tipoInstrumento: PartialUpdateTipoInstrumento): Observable<ITipoInstrumento> {
    const copy = this.convertValueFromClient(tipoInstrumento);
    return this.http
      .patch<RestTipoInstrumento>(`${this.resourceUrl}/${encodeURIComponent(this.getTipoInstrumentoIdentifier(tipoInstrumento))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ITipoInstrumento> {
    return this.http
      .get<RestTipoInstrumento>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ITipoInstrumento[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTipoInstrumento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTipoInstrumentoIdentifier(tipoInstrumento: Pick<ITipoInstrumento, 'id'>): number {
    return tipoInstrumento.id;
  }

  compareTipoInstrumento(o1: Pick<ITipoInstrumento, 'id'> | null, o2: Pick<ITipoInstrumento, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoInstrumentoIdentifier(o1) === this.getTipoInstrumentoIdentifier(o2) : o1 === o2;
  }

  addTipoInstrumentoToCollectionIfMissing<Type extends Pick<ITipoInstrumento, 'id'>>(
    tipoInstrumentoCollection: Type[],
    ...tipoInstrumentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoInstrumentos: Type[] = tipoInstrumentosToCheck.filter(
      tipoInstrumentoItem => tipoInstrumentoItem !== null && tipoInstrumentoItem !== undefined,
    );
    if (tipoInstrumentos.length > 0) {
      const tipoInstrumentoCollectionIdentifiers = tipoInstrumentoCollection.map(tipoInstrumentoItem =>
        this.getTipoInstrumentoIdentifier(tipoInstrumentoItem),
      );
      const tipoInstrumentosToAdd = tipoInstrumentos.filter(tipoInstrumentoItem => {
        const tipoInstrumentoIdentifier = this.getTipoInstrumentoIdentifier(tipoInstrumentoItem);
        if (tipoInstrumentoCollectionIdentifiers.includes(tipoInstrumentoIdentifier)) {
          return false;
        }
        tipoInstrumentoCollectionIdentifiers.push(tipoInstrumentoIdentifier);
        return true;
      });
      return [...tipoInstrumentosToAdd, ...tipoInstrumentoCollection];
    }
    return tipoInstrumentoCollection;
  }

  protected convertValueFromClient<T extends ITipoInstrumento | NewTipoInstrumento | PartialUpdateTipoInstrumento>(
    tipoInstrumento: T,
  ): RestOf<T> {
    return {
      ...tipoInstrumento,
      fechaCreacion: tipoInstrumento.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: tipoInstrumento.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestTipoInstrumento): ITipoInstrumento {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestTipoInstrumento[]): ITipoInstrumento[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
