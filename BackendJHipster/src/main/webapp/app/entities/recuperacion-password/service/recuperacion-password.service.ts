import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IRecuperacionPassword, NewRecuperacionPassword } from '../recuperacion-password.model';

export type PartialUpdateRecuperacionPassword = Partial<IRecuperacionPassword> & Pick<IRecuperacionPassword, 'id'>;

type RestOf<T extends IRecuperacionPassword | NewRecuperacionPassword> = Omit<T, 'fechaSolicitud' | 'fechaExpiracion'> & {
  fechaSolicitud?: string | null;
  fechaExpiracion?: string | null;
};

export type RestRecuperacionPassword = RestOf<IRecuperacionPassword>;

export type NewRestRecuperacionPassword = RestOf<NewRecuperacionPassword>;

export type PartialUpdateRestRecuperacionPassword = RestOf<PartialUpdateRecuperacionPassword>;

@Service()
export class RecuperacionPasswordsService {
  readonly recuperacionPasswordsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly recuperacionPasswordsResource = httpResource<RestRecuperacionPassword[]>(() => {
    const params = this.recuperacionPasswordsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of recuperacionPassword that have been fetched. It is updated when the recuperacionPasswordsResource emits a new value.
   * In case of error while fetching the recuperacionPasswords, the signal is set to an empty array.
   */
  readonly recuperacionPasswords = computed(() =>
    (this.recuperacionPasswordsResource.hasValue() ? this.recuperacionPasswordsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/recuperacion-passwords`;

  protected convertValueFromServer(restRecuperacionPassword: RestRecuperacionPassword): IRecuperacionPassword {
    return {
      ...restRecuperacionPassword,
      fechaSolicitud: restRecuperacionPassword.fechaSolicitud ? dayjs(restRecuperacionPassword.fechaSolicitud) : undefined,
      fechaExpiracion: restRecuperacionPassword.fechaExpiracion ? dayjs(restRecuperacionPassword.fechaExpiracion) : undefined,
    };
  }
}

@Service()
export class RecuperacionPasswordService extends RecuperacionPasswordsService {
  protected readonly http = inject(HttpClient);

  create(recuperacionPassword: NewRecuperacionPassword): Observable<IRecuperacionPassword> {
    const copy = this.convertValueFromClient(recuperacionPassword);
    return this.http.post<RestRecuperacionPassword>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(recuperacionPassword: IRecuperacionPassword): Observable<IRecuperacionPassword> {
    const copy = this.convertValueFromClient(recuperacionPassword);
    return this.http
      .put<RestRecuperacionPassword>(
        `${this.resourceUrl}/${encodeURIComponent(this.getRecuperacionPasswordIdentifier(recuperacionPassword))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(recuperacionPassword: PartialUpdateRecuperacionPassword): Observable<IRecuperacionPassword> {
    const copy = this.convertValueFromClient(recuperacionPassword);
    return this.http
      .patch<RestRecuperacionPassword>(
        `${this.resourceUrl}/${encodeURIComponent(this.getRecuperacionPasswordIdentifier(recuperacionPassword))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IRecuperacionPassword> {
    return this.http
      .get<RestRecuperacionPassword>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IRecuperacionPassword[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRecuperacionPassword[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getRecuperacionPasswordIdentifier(recuperacionPassword: Pick<IRecuperacionPassword, 'id'>): number {
    return recuperacionPassword.id;
  }

  compareRecuperacionPassword(o1: Pick<IRecuperacionPassword, 'id'> | null, o2: Pick<IRecuperacionPassword, 'id'> | null): boolean {
    return o1 && o2 ? this.getRecuperacionPasswordIdentifier(o1) === this.getRecuperacionPasswordIdentifier(o2) : o1 === o2;
  }

  addRecuperacionPasswordToCollectionIfMissing<Type extends Pick<IRecuperacionPassword, 'id'>>(
    recuperacionPasswordCollection: Type[],
    ...recuperacionPasswordsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const recuperacionPasswords: Type[] = recuperacionPasswordsToCheck.filter(
      recuperacionPasswordItem => recuperacionPasswordItem !== null && recuperacionPasswordItem !== undefined,
    );
    if (recuperacionPasswords.length > 0) {
      const recuperacionPasswordCollectionIdentifiers = recuperacionPasswordCollection.map(recuperacionPasswordItem =>
        this.getRecuperacionPasswordIdentifier(recuperacionPasswordItem),
      );
      const recuperacionPasswordsToAdd = recuperacionPasswords.filter(recuperacionPasswordItem => {
        const recuperacionPasswordIdentifier = this.getRecuperacionPasswordIdentifier(recuperacionPasswordItem);
        if (recuperacionPasswordCollectionIdentifiers.includes(recuperacionPasswordIdentifier)) {
          return false;
        }
        recuperacionPasswordCollectionIdentifiers.push(recuperacionPasswordIdentifier);
        return true;
      });
      return [...recuperacionPasswordsToAdd, ...recuperacionPasswordCollection];
    }
    return recuperacionPasswordCollection;
  }

  protected convertValueFromClient<T extends IRecuperacionPassword | NewRecuperacionPassword | PartialUpdateRecuperacionPassword>(
    recuperacionPassword: T,
  ): RestOf<T> {
    return {
      ...recuperacionPassword,
      fechaSolicitud: recuperacionPassword.fechaSolicitud?.toJSON() ?? null,
      fechaExpiracion: recuperacionPassword.fechaExpiracion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestRecuperacionPassword): IRecuperacionPassword {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestRecuperacionPassword[]): IRecuperacionPassword[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
