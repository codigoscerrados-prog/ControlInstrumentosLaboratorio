import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IActivo } from '../activo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../activo.test-samples';

import { ActivoService, RestActivo } from './activo.service';

const requireRestSample: RestActivo = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('Activo Service', () => {
  let service: ActivoService;
  let httpMock: HttpTestingController;
  let expectedResult: IActivo | IActivo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ActivoService);
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

    it('should create a Activo', () => {
      const activo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(activo).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Activo', () => {
      const activo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(activo).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Activo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Activo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Activo', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addActivoToCollectionIfMissing', () => {
      it('should add a Activo to an empty array', () => {
        const activo: IActivo = sampleWithRequiredData;
        expectedResult = service.addActivoToCollectionIfMissing([], activo);
        expect(expectedResult).toEqual([activo]);
      });

      it('should not add a Activo to an array that contains it', () => {
        const activo: IActivo = sampleWithRequiredData;
        const activoCollection: IActivo[] = [
          {
            ...activo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addActivoToCollectionIfMissing(activoCollection, activo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Activo to an array that doesn't contain it", () => {
        const activo: IActivo = sampleWithRequiredData;
        const activoCollection: IActivo[] = [sampleWithPartialData];
        expectedResult = service.addActivoToCollectionIfMissing(activoCollection, activo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activo);
      });

      it('should add only unique Activo to an array', () => {
        const activoArray: IActivo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const activoCollection: IActivo[] = [sampleWithRequiredData];
        expectedResult = service.addActivoToCollectionIfMissing(activoCollection, ...activoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const activo: IActivo = sampleWithRequiredData;
        const activo2: IActivo = sampleWithPartialData;
        expectedResult = service.addActivoToCollectionIfMissing([], activo, activo2);
        expect(expectedResult).toEqual([activo, activo2]);
      });

      it('should accept null and undefined values', () => {
        const activo: IActivo = sampleWithRequiredData;
        expectedResult = service.addActivoToCollectionIfMissing([], null, activo, undefined);
        expect(expectedResult).toEqual([activo]);
      });

      it('should return initial array if no Activo is added', () => {
        const activoCollection: IActivo[] = [sampleWithRequiredData];
        expectedResult = service.addActivoToCollectionIfMissing(activoCollection, undefined, null);
        expect(expectedResult).toEqual(activoCollection);
      });
    });

    describe('compareActivo', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareActivo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5352 };
        const entity2 = null;

        const compareResult1 = service.compareActivo(entity1, entity2);
        const compareResult2 = service.compareActivo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5352 };
        const entity2 = { id: 13823 };

        const compareResult1 = service.compareActivo(entity1, entity2);
        const compareResult2 = service.compareActivo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 5352 };
        const entity2 = { id: 5352 };

        const compareResult1 = service.compareActivo(entity1, entity2);
        const compareResult2 = service.compareActivo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
