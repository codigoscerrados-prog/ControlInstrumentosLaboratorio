import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IAlumno } from '../alumno.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../alumno.test-samples';

import { AlumnoService, RestAlumno } from './alumno.service';

const requireRestSample: RestAlumno = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('Alumno Service', () => {
  let service: AlumnoService;
  let httpMock: HttpTestingController;
  let expectedResult: IAlumno | IAlumno[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AlumnoService);
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

    it('should create a Alumno', () => {
      const alumno = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(alumno).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Alumno', () => {
      const alumno = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(alumno).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Alumno', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Alumno', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Alumno', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addAlumnoToCollectionIfMissing', () => {
      it('should add a Alumno to an empty array', () => {
        const alumno: IAlumno = sampleWithRequiredData;
        expectedResult = service.addAlumnoToCollectionIfMissing([], alumno);
        expect(expectedResult).toEqual([alumno]);
      });

      it('should not add a Alumno to an array that contains it', () => {
        const alumno: IAlumno = sampleWithRequiredData;
        const alumnoCollection: IAlumno[] = [
          {
            ...alumno,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAlumnoToCollectionIfMissing(alumnoCollection, alumno);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Alumno to an array that doesn't contain it", () => {
        const alumno: IAlumno = sampleWithRequiredData;
        const alumnoCollection: IAlumno[] = [sampleWithPartialData];
        expectedResult = service.addAlumnoToCollectionIfMissing(alumnoCollection, alumno);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alumno);
      });

      it('should add only unique Alumno to an array', () => {
        const alumnoArray: IAlumno[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const alumnoCollection: IAlumno[] = [sampleWithRequiredData];
        expectedResult = service.addAlumnoToCollectionIfMissing(alumnoCollection, ...alumnoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alumno: IAlumno = sampleWithRequiredData;
        const alumno2: IAlumno = sampleWithPartialData;
        expectedResult = service.addAlumnoToCollectionIfMissing([], alumno, alumno2);
        expect(expectedResult).toEqual([alumno, alumno2]);
      });

      it('should accept null and undefined values', () => {
        const alumno: IAlumno = sampleWithRequiredData;
        expectedResult = service.addAlumnoToCollectionIfMissing([], null, alumno, undefined);
        expect(expectedResult).toEqual([alumno]);
      });

      it('should return initial array if no Alumno is added', () => {
        const alumnoCollection: IAlumno[] = [sampleWithRequiredData];
        expectedResult = service.addAlumnoToCollectionIfMissing(alumnoCollection, undefined, null);
        expect(expectedResult).toEqual(alumnoCollection);
      });
    });

    describe('compareAlumno', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAlumno(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25216 };
        const entity2 = null;

        const compareResult1 = service.compareAlumno(entity1, entity2);
        const compareResult2 = service.compareAlumno(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25216 };
        const entity2 = { id: 12601 };

        const compareResult1 = service.compareAlumno(entity1, entity2);
        const compareResult2 = service.compareAlumno(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 25216 };
        const entity2 = { id: 25216 };

        const compareResult1 = service.compareAlumno(entity1, entity2);
        const compareResult2 = service.compareAlumno(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
