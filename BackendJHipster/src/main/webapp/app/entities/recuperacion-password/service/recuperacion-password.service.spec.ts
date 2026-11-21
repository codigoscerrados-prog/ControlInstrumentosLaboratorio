import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IRecuperacionPassword } from '../recuperacion-password.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../recuperacion-password.test-samples';

import { RecuperacionPasswordService, RestRecuperacionPassword } from './recuperacion-password.service';

const requireRestSample: RestRecuperacionPassword = {
  ...sampleWithRequiredData,
  fechaSolicitud: sampleWithRequiredData.fechaSolicitud?.toJSON(),
  fechaExpiracion: sampleWithRequiredData.fechaExpiracion?.toJSON(),
};

describe('RecuperacionPassword Service', () => {
  let service: RecuperacionPasswordService;
  let httpMock: HttpTestingController;
  let expectedResult: IRecuperacionPassword | IRecuperacionPassword[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RecuperacionPasswordService);
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

    it('should create a RecuperacionPassword', () => {
      const recuperacionPassword = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(recuperacionPassword).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RecuperacionPassword', () => {
      const recuperacionPassword = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(recuperacionPassword).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RecuperacionPassword', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RecuperacionPassword', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RecuperacionPassword', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addRecuperacionPasswordToCollectionIfMissing', () => {
      it('should add a RecuperacionPassword to an empty array', () => {
        const recuperacionPassword: IRecuperacionPassword = sampleWithRequiredData;
        expectedResult = service.addRecuperacionPasswordToCollectionIfMissing([], recuperacionPassword);
        expect(expectedResult).toEqual([recuperacionPassword]);
      });

      it('should not add a RecuperacionPassword to an array that contains it', () => {
        const recuperacionPassword: IRecuperacionPassword = sampleWithRequiredData;
        const recuperacionPasswordCollection: IRecuperacionPassword[] = [
          {
            ...recuperacionPassword,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRecuperacionPasswordToCollectionIfMissing(recuperacionPasswordCollection, recuperacionPassword);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RecuperacionPassword to an array that doesn't contain it", () => {
        const recuperacionPassword: IRecuperacionPassword = sampleWithRequiredData;
        const recuperacionPasswordCollection: IRecuperacionPassword[] = [sampleWithPartialData];
        expectedResult = service.addRecuperacionPasswordToCollectionIfMissing(recuperacionPasswordCollection, recuperacionPassword);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recuperacionPassword);
      });

      it('should add only unique RecuperacionPassword to an array', () => {
        const recuperacionPasswordArray: IRecuperacionPassword[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const recuperacionPasswordCollection: IRecuperacionPassword[] = [sampleWithRequiredData];
        expectedResult = service.addRecuperacionPasswordToCollectionIfMissing(recuperacionPasswordCollection, ...recuperacionPasswordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recuperacionPassword: IRecuperacionPassword = sampleWithRequiredData;
        const recuperacionPassword2: IRecuperacionPassword = sampleWithPartialData;
        expectedResult = service.addRecuperacionPasswordToCollectionIfMissing([], recuperacionPassword, recuperacionPassword2);
        expect(expectedResult).toEqual([recuperacionPassword, recuperacionPassword2]);
      });

      it('should accept null and undefined values', () => {
        const recuperacionPassword: IRecuperacionPassword = sampleWithRequiredData;
        expectedResult = service.addRecuperacionPasswordToCollectionIfMissing([], null, recuperacionPassword, undefined);
        expect(expectedResult).toEqual([recuperacionPassword]);
      });

      it('should return initial array if no RecuperacionPassword is added', () => {
        const recuperacionPasswordCollection: IRecuperacionPassword[] = [sampleWithRequiredData];
        expectedResult = service.addRecuperacionPasswordToCollectionIfMissing(recuperacionPasswordCollection, undefined, null);
        expect(expectedResult).toEqual(recuperacionPasswordCollection);
      });
    });

    describe('compareRecuperacionPassword', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRecuperacionPassword(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 27296 };
        const entity2 = null;

        const compareResult1 = service.compareRecuperacionPassword(entity1, entity2);
        const compareResult2 = service.compareRecuperacionPassword(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 27296 };
        const entity2 = { id: 29135 };

        const compareResult1 = service.compareRecuperacionPassword(entity1, entity2);
        const compareResult2 = service.compareRecuperacionPassword(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 27296 };
        const entity2 = { id: 27296 };

        const compareResult1 = service.compareRecuperacionPassword(entity1, entity2);
        const compareResult2 = service.compareRecuperacionPassword(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
