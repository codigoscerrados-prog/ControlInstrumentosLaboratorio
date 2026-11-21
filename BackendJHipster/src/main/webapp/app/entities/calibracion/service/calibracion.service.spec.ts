import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { ICalibracion } from '../calibracion.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../calibracion.test-samples';

import { CalibracionService, RestCalibracion } from './calibracion.service';

const requireRestSample: RestCalibracion = {
  ...sampleWithRequiredData,
  fechaEnvio: sampleWithRequiredData.fechaEnvio?.format(DATE_FORMAT),
  fechaCalibracion: sampleWithRequiredData.fechaCalibracion?.format(DATE_FORMAT),
  fechaRetorno: sampleWithRequiredData.fechaRetorno?.format(DATE_FORMAT),
  fechaVencimiento: sampleWithRequiredData.fechaVencimiento?.format(DATE_FORMAT),
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('Calibracion Service', () => {
  let service: CalibracionService;
  let httpMock: HttpTestingController;
  let expectedResult: ICalibracion | ICalibracion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CalibracionService);
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

    it('should create a Calibracion', () => {
      const calibracion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(calibracion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Calibracion', () => {
      const calibracion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(calibracion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Calibracion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Calibracion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Calibracion', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCalibracionToCollectionIfMissing', () => {
      it('should add a Calibracion to an empty array', () => {
        const calibracion: ICalibracion = sampleWithRequiredData;
        expectedResult = service.addCalibracionToCollectionIfMissing([], calibracion);
        expect(expectedResult).toEqual([calibracion]);
      });

      it('should not add a Calibracion to an array that contains it', () => {
        const calibracion: ICalibracion = sampleWithRequiredData;
        const calibracionCollection: ICalibracion[] = [
          {
            ...calibracion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCalibracionToCollectionIfMissing(calibracionCollection, calibracion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Calibracion to an array that doesn't contain it", () => {
        const calibracion: ICalibracion = sampleWithRequiredData;
        const calibracionCollection: ICalibracion[] = [sampleWithPartialData];
        expectedResult = service.addCalibracionToCollectionIfMissing(calibracionCollection, calibracion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(calibracion);
      });

      it('should add only unique Calibracion to an array', () => {
        const calibracionArray: ICalibracion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const calibracionCollection: ICalibracion[] = [sampleWithRequiredData];
        expectedResult = service.addCalibracionToCollectionIfMissing(calibracionCollection, ...calibracionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const calibracion: ICalibracion = sampleWithRequiredData;
        const calibracion2: ICalibracion = sampleWithPartialData;
        expectedResult = service.addCalibracionToCollectionIfMissing([], calibracion, calibracion2);
        expect(expectedResult).toEqual([calibracion, calibracion2]);
      });

      it('should accept null and undefined values', () => {
        const calibracion: ICalibracion = sampleWithRequiredData;
        expectedResult = service.addCalibracionToCollectionIfMissing([], null, calibracion, undefined);
        expect(expectedResult).toEqual([calibracion]);
      });

      it('should return initial array if no Calibracion is added', () => {
        const calibracionCollection: ICalibracion[] = [sampleWithRequiredData];
        expectedResult = service.addCalibracionToCollectionIfMissing(calibracionCollection, undefined, null);
        expect(expectedResult).toEqual(calibracionCollection);
      });
    });

    describe('compareCalibracion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCalibracion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 10837 };
        const entity2 = null;

        const compareResult1 = service.compareCalibracion(entity1, entity2);
        const compareResult2 = service.compareCalibracion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 10837 };
        const entity2 = { id: 29846 };

        const compareResult1 = service.compareCalibracion(entity1, entity2);
        const compareResult2 = service.compareCalibracion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 10837 };
        const entity2 = { id: 10837 };

        const compareResult1 = service.compareCalibracion(entity1, entity2);
        const compareResult2 = service.compareCalibracion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
