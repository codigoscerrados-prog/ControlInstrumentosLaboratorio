import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICursos } from '../cursos.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cursos.test-samples';

import { CursosService, RestCursos } from './cursos.service';

const requireRestSample: RestCursos = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('Cursos Service', () => {
  let service: CursosService;
  let httpMock: HttpTestingController;
  let expectedResult: ICursos | ICursos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CursosService);
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

    it('should create a Cursos', () => {
      const cursos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cursos).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cursos', () => {
      const cursos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cursos).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cursos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cursos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cursos', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCursosToCollectionIfMissing', () => {
      it('should add a Cursos to an empty array', () => {
        const cursos: ICursos = sampleWithRequiredData;
        expectedResult = service.addCursosToCollectionIfMissing([], cursos);
        expect(expectedResult).toEqual([cursos]);
      });

      it('should not add a Cursos to an array that contains it', () => {
        const cursos: ICursos = sampleWithRequiredData;
        const cursosCollection: ICursos[] = [
          {
            ...cursos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCursosToCollectionIfMissing(cursosCollection, cursos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cursos to an array that doesn't contain it", () => {
        const cursos: ICursos = sampleWithRequiredData;
        const cursosCollection: ICursos[] = [sampleWithPartialData];
        expectedResult = service.addCursosToCollectionIfMissing(cursosCollection, cursos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cursos);
      });

      it('should add only unique Cursos to an array', () => {
        const cursosArray: ICursos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cursosCollection: ICursos[] = [sampleWithRequiredData];
        expectedResult = service.addCursosToCollectionIfMissing(cursosCollection, ...cursosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cursos: ICursos = sampleWithRequiredData;
        const cursos2: ICursos = sampleWithPartialData;
        expectedResult = service.addCursosToCollectionIfMissing([], cursos, cursos2);
        expect(expectedResult).toEqual([cursos, cursos2]);
      });

      it('should accept null and undefined values', () => {
        const cursos: ICursos = sampleWithRequiredData;
        expectedResult = service.addCursosToCollectionIfMissing([], null, cursos, undefined);
        expect(expectedResult).toEqual([cursos]);
      });

      it('should return initial array if no Cursos is added', () => {
        const cursosCollection: ICursos[] = [sampleWithRequiredData];
        expectedResult = service.addCursosToCollectionIfMissing(cursosCollection, undefined, null);
        expect(expectedResult).toEqual(cursosCollection);
      });
    });

    describe('compareCursos', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCursos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8714 };
        const entity2 = null;

        const compareResult1 = service.compareCursos(entity1, entity2);
        const compareResult2 = service.compareCursos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8714 };
        const entity2 = { id: 27258 };

        const compareResult1 = service.compareCursos(entity1, entity2);
        const compareResult2 = service.compareCursos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 8714 };
        const entity2 = { id: 8714 };

        const compareResult1 = service.compareCursos(entity1, entity2);
        const compareResult2 = service.compareCursos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
