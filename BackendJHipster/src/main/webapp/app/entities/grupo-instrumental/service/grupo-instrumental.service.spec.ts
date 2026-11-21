import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IGrupoInstrumental } from '../grupo-instrumental.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../grupo-instrumental.test-samples';

import { GrupoInstrumentalService, RestGrupoInstrumental } from './grupo-instrumental.service';

const requireRestSample: RestGrupoInstrumental = {
  ...sampleWithRequiredData,
  fechaCreacion: sampleWithRequiredData.fechaCreacion?.toJSON(),
  fechaModificacion: sampleWithRequiredData.fechaModificacion?.toJSON(),
};

describe('GrupoInstrumental Service', () => {
  let service: GrupoInstrumentalService;
  let httpMock: HttpTestingController;
  let expectedResult: IGrupoInstrumental | IGrupoInstrumental[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GrupoInstrumentalService);
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

    it('should create a GrupoInstrumental', () => {
      const grupoInstrumental = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(grupoInstrumental).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GrupoInstrumental', () => {
      const grupoInstrumental = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(grupoInstrumental).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GrupoInstrumental', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GrupoInstrumental', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GrupoInstrumental', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addGrupoInstrumentalToCollectionIfMissing', () => {
      it('should add a GrupoInstrumental to an empty array', () => {
        const grupoInstrumental: IGrupoInstrumental = sampleWithRequiredData;
        expectedResult = service.addGrupoInstrumentalToCollectionIfMissing([], grupoInstrumental);
        expect(expectedResult).toEqual([grupoInstrumental]);
      });

      it('should not add a GrupoInstrumental to an array that contains it', () => {
        const grupoInstrumental: IGrupoInstrumental = sampleWithRequiredData;
        const grupoInstrumentalCollection: IGrupoInstrumental[] = [
          {
            ...grupoInstrumental,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGrupoInstrumentalToCollectionIfMissing(grupoInstrumentalCollection, grupoInstrumental);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GrupoInstrumental to an array that doesn't contain it", () => {
        const grupoInstrumental: IGrupoInstrumental = sampleWithRequiredData;
        const grupoInstrumentalCollection: IGrupoInstrumental[] = [sampleWithPartialData];
        expectedResult = service.addGrupoInstrumentalToCollectionIfMissing(grupoInstrumentalCollection, grupoInstrumental);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grupoInstrumental);
      });

      it('should add only unique GrupoInstrumental to an array', () => {
        const grupoInstrumentalArray: IGrupoInstrumental[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const grupoInstrumentalCollection: IGrupoInstrumental[] = [sampleWithRequiredData];
        expectedResult = service.addGrupoInstrumentalToCollectionIfMissing(grupoInstrumentalCollection, ...grupoInstrumentalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const grupoInstrumental: IGrupoInstrumental = sampleWithRequiredData;
        const grupoInstrumental2: IGrupoInstrumental = sampleWithPartialData;
        expectedResult = service.addGrupoInstrumentalToCollectionIfMissing([], grupoInstrumental, grupoInstrumental2);
        expect(expectedResult).toEqual([grupoInstrumental, grupoInstrumental2]);
      });

      it('should accept null and undefined values', () => {
        const grupoInstrumental: IGrupoInstrumental = sampleWithRequiredData;
        expectedResult = service.addGrupoInstrumentalToCollectionIfMissing([], null, grupoInstrumental, undefined);
        expect(expectedResult).toEqual([grupoInstrumental]);
      });

      it('should return initial array if no GrupoInstrumental is added', () => {
        const grupoInstrumentalCollection: IGrupoInstrumental[] = [sampleWithRequiredData];
        expectedResult = service.addGrupoInstrumentalToCollectionIfMissing(grupoInstrumentalCollection, undefined, null);
        expect(expectedResult).toEqual(grupoInstrumentalCollection);
      });
    });

    describe('compareGrupoInstrumental', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGrupoInstrumental(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30752 };
        const entity2 = null;

        const compareResult1 = service.compareGrupoInstrumental(entity1, entity2);
        const compareResult2 = service.compareGrupoInstrumental(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30752 };
        const entity2 = { id: 31278 };

        const compareResult1 = service.compareGrupoInstrumental(entity1, entity2);
        const compareResult2 = service.compareGrupoInstrumental(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 30752 };
        const entity2 = { id: 30752 };

        const compareResult1 = service.compareGrupoInstrumental(entity1, entity2);
        const compareResult2 = service.compareGrupoInstrumental(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
