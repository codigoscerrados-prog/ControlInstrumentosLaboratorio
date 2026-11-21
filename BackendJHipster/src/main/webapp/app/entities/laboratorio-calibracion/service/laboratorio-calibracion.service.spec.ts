import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ILaboratorioCalibracion } from '../laboratorio-calibracion.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../laboratorio-calibracion.test-samples';

import { LaboratorioCalibracionService, RestLaboratorioCalibracion } from './laboratorio-calibracion.service';

const requireRestSample: RestLaboratorioCalibracion = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('LaboratorioCalibracion Service', () => {
  let service: LaboratorioCalibracionService;
  let httpMock: HttpTestingController;
  let expectedResult: ILaboratorioCalibracion | ILaboratorioCalibracion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LaboratorioCalibracionService);
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

    it('should create a LaboratorioCalibracion', () => {
      const laboratorioCalibracion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(laboratorioCalibracion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LaboratorioCalibracion', () => {
      const laboratorioCalibracion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(laboratorioCalibracion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LaboratorioCalibracion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LaboratorioCalibracion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LaboratorioCalibracion', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addLaboratorioCalibracionToCollectionIfMissing', () => {
      it('should add a LaboratorioCalibracion to an empty array', () => {
        const laboratorioCalibracion: ILaboratorioCalibracion = sampleWithRequiredData;
        expectedResult = service.addLaboratorioCalibracionToCollectionIfMissing([], laboratorioCalibracion);
        expect(expectedResult).toEqual([laboratorioCalibracion]);
      });

      it('should not add a LaboratorioCalibracion to an array that contains it', () => {
        const laboratorioCalibracion: ILaboratorioCalibracion = sampleWithRequiredData;
        const laboratorioCalibracionCollection: ILaboratorioCalibracion[] = [
          {
            ...laboratorioCalibracion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLaboratorioCalibracionToCollectionIfMissing(laboratorioCalibracionCollection, laboratorioCalibracion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LaboratorioCalibracion to an array that doesn't contain it", () => {
        const laboratorioCalibracion: ILaboratorioCalibracion = sampleWithRequiredData;
        const laboratorioCalibracionCollection: ILaboratorioCalibracion[] = [sampleWithPartialData];
        expectedResult = service.addLaboratorioCalibracionToCollectionIfMissing(laboratorioCalibracionCollection, laboratorioCalibracion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(laboratorioCalibracion);
      });

      it('should add only unique LaboratorioCalibracion to an array', () => {
        const laboratorioCalibracionArray: ILaboratorioCalibracion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const laboratorioCalibracionCollection: ILaboratorioCalibracion[] = [sampleWithRequiredData];
        expectedResult = service.addLaboratorioCalibracionToCollectionIfMissing(
          laboratorioCalibracionCollection,
          ...laboratorioCalibracionArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const laboratorioCalibracion: ILaboratorioCalibracion = sampleWithRequiredData;
        const laboratorioCalibracion2: ILaboratorioCalibracion = sampleWithPartialData;
        expectedResult = service.addLaboratorioCalibracionToCollectionIfMissing([], laboratorioCalibracion, laboratorioCalibracion2);
        expect(expectedResult).toEqual([laboratorioCalibracion, laboratorioCalibracion2]);
      });

      it('should accept null and undefined values', () => {
        const laboratorioCalibracion: ILaboratorioCalibracion = sampleWithRequiredData;
        expectedResult = service.addLaboratorioCalibracionToCollectionIfMissing([], null, laboratorioCalibracion, undefined);
        expect(expectedResult).toEqual([laboratorioCalibracion]);
      });

      it('should return initial array if no LaboratorioCalibracion is added', () => {
        const laboratorioCalibracionCollection: ILaboratorioCalibracion[] = [sampleWithRequiredData];
        expectedResult = service.addLaboratorioCalibracionToCollectionIfMissing(laboratorioCalibracionCollection, undefined, null);
        expect(expectedResult).toEqual(laboratorioCalibracionCollection);
      });
    });

    describe('compareLaboratorioCalibracion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLaboratorioCalibracion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6359 };
        const entity2 = null;

        const compareResult1 = service.compareLaboratorioCalibracion(entity1, entity2);
        const compareResult2 = service.compareLaboratorioCalibracion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6359 };
        const entity2 = { id: 14709 };

        const compareResult1 = service.compareLaboratorioCalibracion(entity1, entity2);
        const compareResult2 = service.compareLaboratorioCalibracion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 6359 };
        const entity2 = { id: 6359 };

        const compareResult1 = service.compareLaboratorioCalibracion(entity1, entity2);
        const compareResult2 = service.compareLaboratorioCalibracion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
