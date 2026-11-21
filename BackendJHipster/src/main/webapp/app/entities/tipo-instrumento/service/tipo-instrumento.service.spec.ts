import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITipoInstrumento } from '../tipo-instrumento.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tipo-instrumento.test-samples';

import { RestTipoInstrumento, TipoInstrumentoService } from './tipo-instrumento.service';

const requireRestSample: RestTipoInstrumento = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('TipoInstrumento Service', () => {
  let service: TipoInstrumentoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITipoInstrumento | ITipoInstrumento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TipoInstrumentoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TipoInstrumento', () => {
      const tipoInstrumento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tipoInstrumento).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoInstrumento', () => {
      const tipoInstrumento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tipoInstrumento).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoInstrumento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoInstrumento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TipoInstrumento', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTipoInstrumentoToCollectionIfMissing', () => {
      it('should add a TipoInstrumento to an empty array', () => {
        const tipoInstrumento: ITipoInstrumento = sampleWithRequiredData;
        expectedResult = service.addTipoInstrumentoToCollectionIfMissing([], tipoInstrumento);
        expect(expectedResult).toEqual([tipoInstrumento]);
      });

      it('should not add a TipoInstrumento to an array that contains it', () => {
        const tipoInstrumento: ITipoInstrumento = sampleWithRequiredData;
        const tipoInstrumentoCollection: ITipoInstrumento[] = [
          {
            ...tipoInstrumento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTipoInstrumentoToCollectionIfMissing(tipoInstrumentoCollection, tipoInstrumento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoInstrumento to an array that doesn't contain it", () => {
        const tipoInstrumento: ITipoInstrumento = sampleWithRequiredData;
        const tipoInstrumentoCollection: ITipoInstrumento[] = [sampleWithPartialData];
        expectedResult = service.addTipoInstrumentoToCollectionIfMissing(tipoInstrumentoCollection, tipoInstrumento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoInstrumento);
      });

      it('should add only unique TipoInstrumento to an array', () => {
        const tipoInstrumentoArray: ITipoInstrumento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tipoInstrumentoCollection: ITipoInstrumento[] = [sampleWithRequiredData];
        expectedResult = service.addTipoInstrumentoToCollectionIfMissing(tipoInstrumentoCollection, ...tipoInstrumentoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoInstrumento: ITipoInstrumento = sampleWithRequiredData;
        const tipoInstrumento2: ITipoInstrumento = sampleWithPartialData;
        expectedResult = service.addTipoInstrumentoToCollectionIfMissing([], tipoInstrumento, tipoInstrumento2);
        expect(expectedResult).toEqual([tipoInstrumento, tipoInstrumento2]);
      });

      it('should accept null and undefined values', () => {
        const tipoInstrumento: ITipoInstrumento = sampleWithRequiredData;
        expectedResult = service.addTipoInstrumentoToCollectionIfMissing([], null, tipoInstrumento, undefined);
        expect(expectedResult).toEqual([tipoInstrumento]);
      });

      it('should return initial array if no TipoInstrumento is added', () => {
        const tipoInstrumentoCollection: ITipoInstrumento[] = [sampleWithRequiredData];
        expectedResult = service.addTipoInstrumentoToCollectionIfMissing(tipoInstrumentoCollection, undefined, null);
        expect(expectedResult).toEqual(tipoInstrumentoCollection);
      });
    });

    describe('compareTipoInstrumento', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTipoInstrumento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2947 };
        const entity2 = null;

        const compareResult1 = service.compareTipoInstrumento(entity1, entity2);
        const compareResult2 = service.compareTipoInstrumento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2947 };
        const entity2 = { id: 1504 };

        const compareResult1 = service.compareTipoInstrumento(entity1, entity2);
        const compareResult2 = service.compareTipoInstrumento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 2947 };
        const entity2 = { id: 2947 };

        const compareResult1 = service.compareTipoInstrumento(entity1, entity2);
        const compareResult2 = service.compareTipoInstrumento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
