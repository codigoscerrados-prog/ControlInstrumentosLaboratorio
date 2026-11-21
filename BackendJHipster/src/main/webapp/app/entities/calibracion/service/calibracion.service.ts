import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICalibracion, NewCalibracion } from '../calibracion.model';

export type PartialUpdateCalibracion = Partial<ICalibracion> & Pick<ICalibracion, 'id'>;

type RestOf<T extends ICalibracion | NewCalibracion> = Omit<
  T,
  'fechaEnvio' | 'fechaCalibracion' | 'fechaRetorno' | 'fechaVencimiento' | 'fechaCreacion' | 'fechaModificacion'
> & {
  fechaEnvio?: string | null;
  fechaCalibracion?: string | null;
  fechaRetorno?: string | null;
  fechaVencimiento?: string | null;
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestCalibracion = RestOf<ICalibracion>;

export type NewRestCalibracion = RestOf<NewCalibracion>;

export type PartialUpdateRestCalibracion = RestOf<PartialUpdateCalibracion>;

@Service()
export class CalibracionsService {
  readonly calibracionsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly calibracionsResource = httpResource<RestCalibracion[]>(() => {
    const params = this.calibracionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of calibracion that have been fetched. It is updated when the calibracionsResource emits a new value.
   * In case of error while fetching the calibracions, the signal is set to an empty array.
   */
  readonly calibracions = computed(() =>
    (this.calibracionsResource.hasValue() ? this.calibracionsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/calibracions`;

  protected convertValueFromServer(restCalibracion: RestCalibracion): ICalibracion {
    return {
      ...restCalibracion,
      fechaEnvio: restCalibracion.fechaEnvio ? dayjs(restCalibracion.fechaEnvio) : undefined,
      fechaCalibracion: restCalibracion.fechaCalibracion ? dayjs(restCalibracion.fechaCalibracion) : undefined,
      fechaRetorno: restCalibracion.fechaRetorno ? dayjs(restCalibracion.fechaRetorno) : undefined,
      fechaVencimiento: restCalibracion.fechaVencimiento ? dayjs(restCalibracion.fechaVencimiento) : undefined,
      fechaCreacion: restCalibracion.fechaCreacion ? dayjs(restCalibracion.fechaCreacion) : undefined,
      fechaModificacion: restCalibracion.fechaModificacion ? dayjs(restCalibracion.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class CalibracionService extends CalibracionsService {
  protected readonly http = inject(HttpClient);

  create(calibracion: NewCalibracion): Observable<ICalibracion> {
    const copy = this.convertValueFromClient(calibracion);
    return this.http.post<RestCalibracion>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(calibracion: ICalibracion): Observable<ICalibracion> {
    const copy = this.convertValueFromClient(calibracion);
    return this.http
      .put<RestCalibracion>(`${this.resourceUrl}/${encodeURIComponent(this.getCalibracionIdentifier(calibracion))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(calibracion: PartialUpdateCalibracion): Observable<ICalibracion> {
    const copy = this.convertValueFromClient(calibracion);
    return this.http
      .patch<RestCalibracion>(`${this.resourceUrl}/${encodeURIComponent(this.getCalibracionIdentifier(calibracion))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICalibracion> {
    return this.http
      .get<RestCalibracion>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICalibracion[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCalibracion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCalibracionIdentifier(calibracion: Pick<ICalibracion, 'id'>): number {
    return calibracion.id;
  }

  compareCalibracion(o1: Pick<ICalibracion, 'id'> | null, o2: Pick<ICalibracion, 'id'> | null): boolean {
    return o1 && o2 ? this.getCalibracionIdentifier(o1) === this.getCalibracionIdentifier(o2) : o1 === o2;
  }

  addCalibracionToCollectionIfMissing<Type extends Pick<ICalibracion, 'id'>>(
    calibracionCollection: Type[],
    ...calibracionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const calibracions: Type[] = calibracionsToCheck.filter(calibracionItem => calibracionItem !== null && calibracionItem !== undefined);
    if (calibracions.length > 0) {
      const calibracionCollectionIdentifiers = calibracionCollection.map(calibracionItem => this.getCalibracionIdentifier(calibracionItem));
      const calibracionsToAdd = calibracions.filter(calibracionItem => {
        const calibracionIdentifier = this.getCalibracionIdentifier(calibracionItem);
        if (calibracionCollectionIdentifiers.includes(calibracionIdentifier)) {
          return false;
        }
        calibracionCollectionIdentifiers.push(calibracionIdentifier);
        return true;
      });
      return [...calibracionsToAdd, ...calibracionCollection];
    }
    return calibracionCollection;
  }

  protected convertValueFromClient<T extends ICalibracion | NewCalibracion | PartialUpdateCalibracion>(calibracion: T): RestOf<T> {
    return {
      ...calibracion,
      fechaEnvio: calibracion.fechaEnvio?.format(DATE_FORMAT) ?? null,
      fechaCalibracion: calibracion.fechaCalibracion?.format(DATE_FORMAT) ?? null,
      fechaRetorno: calibracion.fechaRetorno?.format(DATE_FORMAT) ?? null,
      fechaVencimiento: calibracion.fechaVencimiento?.format(DATE_FORMAT) ?? null,
      fechaCreacion: calibracion.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: calibracion.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCalibracion): ICalibracion {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCalibracion[]): ICalibracion[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
