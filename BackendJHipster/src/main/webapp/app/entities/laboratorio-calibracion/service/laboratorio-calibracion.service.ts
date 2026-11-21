import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ILaboratorioCalibracion, NewLaboratorioCalibracion } from '../laboratorio-calibracion.model';

export type PartialUpdateLaboratorioCalibracion = Partial<ILaboratorioCalibracion> & Pick<ILaboratorioCalibracion, 'id'>;

type RestOf<T extends ILaboratorioCalibracion | NewLaboratorioCalibracion> = Omit<T, 'fechaCreacion' | 'fechaModificacion'> & {
  fechaCreacion?: string | null;
  fechaModificacion?: string | null;
};

export type RestLaboratorioCalibracion = RestOf<ILaboratorioCalibracion>;

export type NewRestLaboratorioCalibracion = RestOf<NewLaboratorioCalibracion>;

export type PartialUpdateRestLaboratorioCalibracion = RestOf<PartialUpdateLaboratorioCalibracion>;

@Service()
export class LaboratorioCalibracionsService {
  readonly laboratorioCalibracionsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly laboratorioCalibracionsResource = httpResource<RestLaboratorioCalibracion[]>(() => {
    const params = this.laboratorioCalibracionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of laboratorioCalibracion that have been fetched. It is updated when the laboratorioCalibracionsResource emits a new value.
   * In case of error while fetching the laboratorioCalibracions, the signal is set to an empty array.
   */
  readonly laboratorioCalibracions = computed(() =>
    (this.laboratorioCalibracionsResource.hasValue() ? this.laboratorioCalibracionsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/laboratorio-calibracions`;

  protected convertValueFromServer(restLaboratorioCalibracion: RestLaboratorioCalibracion): ILaboratorioCalibracion {
    return {
      ...restLaboratorioCalibracion,
      fechaCreacion: restLaboratorioCalibracion.fechaCreacion ? dayjs(restLaboratorioCalibracion.fechaCreacion) : undefined,
      fechaModificacion: restLaboratorioCalibracion.fechaModificacion ? dayjs(restLaboratorioCalibracion.fechaModificacion) : undefined,
    };
  }
}

@Service()
export class LaboratorioCalibracionService extends LaboratorioCalibracionsService {
  protected readonly http = inject(HttpClient);

  create(laboratorioCalibracion: NewLaboratorioCalibracion): Observable<ILaboratorioCalibracion> {
    const copy = this.convertValueFromClient(laboratorioCalibracion);
    return this.http.post<RestLaboratorioCalibracion>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(laboratorioCalibracion: ILaboratorioCalibracion): Observable<ILaboratorioCalibracion> {
    const copy = this.convertValueFromClient(laboratorioCalibracion);
    return this.http
      .put<RestLaboratorioCalibracion>(
        `${this.resourceUrl}/${encodeURIComponent(this.getLaboratorioCalibracionIdentifier(laboratorioCalibracion))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(laboratorioCalibracion: PartialUpdateLaboratorioCalibracion): Observable<ILaboratorioCalibracion> {
    const copy = this.convertValueFromClient(laboratorioCalibracion);
    return this.http
      .patch<RestLaboratorioCalibracion>(
        `${this.resourceUrl}/${encodeURIComponent(this.getLaboratorioCalibracionIdentifier(laboratorioCalibracion))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ILaboratorioCalibracion> {
    return this.http
      .get<RestLaboratorioCalibracion>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ILaboratorioCalibracion[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLaboratorioCalibracion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getLaboratorioCalibracionIdentifier(laboratorioCalibracion: Pick<ILaboratorioCalibracion, 'id'>): number {
    return laboratorioCalibracion.id;
  }

  compareLaboratorioCalibracion(o1: Pick<ILaboratorioCalibracion, 'id'> | null, o2: Pick<ILaboratorioCalibracion, 'id'> | null): boolean {
    return o1 && o2 ? this.getLaboratorioCalibracionIdentifier(o1) === this.getLaboratorioCalibracionIdentifier(o2) : o1 === o2;
  }

  addLaboratorioCalibracionToCollectionIfMissing<Type extends Pick<ILaboratorioCalibracion, 'id'>>(
    laboratorioCalibracionCollection: Type[],
    ...laboratorioCalibracionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const laboratorioCalibracions: Type[] = laboratorioCalibracionsToCheck.filter(
      laboratorioCalibracionItem => laboratorioCalibracionItem !== null && laboratorioCalibracionItem !== undefined,
    );
    if (laboratorioCalibracions.length > 0) {
      const laboratorioCalibracionCollectionIdentifiers = laboratorioCalibracionCollection.map(laboratorioCalibracionItem =>
        this.getLaboratorioCalibracionIdentifier(laboratorioCalibracionItem),
      );
      const laboratorioCalibracionsToAdd = laboratorioCalibracions.filter(laboratorioCalibracionItem => {
        const laboratorioCalibracionIdentifier = this.getLaboratorioCalibracionIdentifier(laboratorioCalibracionItem);
        if (laboratorioCalibracionCollectionIdentifiers.includes(laboratorioCalibracionIdentifier)) {
          return false;
        }
        laboratorioCalibracionCollectionIdentifiers.push(laboratorioCalibracionIdentifier);
        return true;
      });
      return [...laboratorioCalibracionsToAdd, ...laboratorioCalibracionCollection];
    }
    return laboratorioCalibracionCollection;
  }

  protected convertValueFromClient<T extends ILaboratorioCalibracion | NewLaboratorioCalibracion | PartialUpdateLaboratorioCalibracion>(
    laboratorioCalibracion: T,
  ): RestOf<T> {
    return {
      ...laboratorioCalibracion,
      fechaCreacion: laboratorioCalibracion.fechaCreacion?.toJSON() ?? null,
      fechaModificacion: laboratorioCalibracion.fechaModificacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestLaboratorioCalibracion): ILaboratorioCalibracion {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestLaboratorioCalibracion[]): ILaboratorioCalibracion[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
