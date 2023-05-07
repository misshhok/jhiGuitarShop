import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGuitarType } from '../guitar-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../guitar-type.test-samples';

import { GuitarTypeService } from './guitar-type.service';

const requireRestSample: IGuitarType = {
  ...sampleWithRequiredData,
};

describe('GuitarType Service', () => {
  let service: GuitarTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IGuitarType | IGuitarType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GuitarTypeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a GuitarType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const guitarType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(guitarType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GuitarType', () => {
      const guitarType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(guitarType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GuitarType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GuitarType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GuitarType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGuitarTypeToCollectionIfMissing', () => {
      it('should add a GuitarType to an empty array', () => {
        const guitarType: IGuitarType = sampleWithRequiredData;
        expectedResult = service.addGuitarTypeToCollectionIfMissing([], guitarType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(guitarType);
      });

      it('should not add a GuitarType to an array that contains it', () => {
        const guitarType: IGuitarType = sampleWithRequiredData;
        const guitarTypeCollection: IGuitarType[] = [
          {
            ...guitarType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGuitarTypeToCollectionIfMissing(guitarTypeCollection, guitarType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GuitarType to an array that doesn't contain it", () => {
        const guitarType: IGuitarType = sampleWithRequiredData;
        const guitarTypeCollection: IGuitarType[] = [sampleWithPartialData];
        expectedResult = service.addGuitarTypeToCollectionIfMissing(guitarTypeCollection, guitarType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(guitarType);
      });

      it('should add only unique GuitarType to an array', () => {
        const guitarTypeArray: IGuitarType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const guitarTypeCollection: IGuitarType[] = [sampleWithRequiredData];
        expectedResult = service.addGuitarTypeToCollectionIfMissing(guitarTypeCollection, ...guitarTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const guitarType: IGuitarType = sampleWithRequiredData;
        const guitarType2: IGuitarType = sampleWithPartialData;
        expectedResult = service.addGuitarTypeToCollectionIfMissing([], guitarType, guitarType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(guitarType);
        expect(expectedResult).toContain(guitarType2);
      });

      it('should accept null and undefined values', () => {
        const guitarType: IGuitarType = sampleWithRequiredData;
        expectedResult = service.addGuitarTypeToCollectionIfMissing([], null, guitarType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(guitarType);
      });

      it('should return initial array if no GuitarType is added', () => {
        const guitarTypeCollection: IGuitarType[] = [sampleWithRequiredData];
        expectedResult = service.addGuitarTypeToCollectionIfMissing(guitarTypeCollection, undefined, null);
        expect(expectedResult).toEqual(guitarTypeCollection);
      });
    });

    describe('compareGuitarType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGuitarType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGuitarType(entity1, entity2);
        const compareResult2 = service.compareGuitarType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGuitarType(entity1, entity2);
        const compareResult2 = service.compareGuitarType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGuitarType(entity1, entity2);
        const compareResult2 = service.compareGuitarType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
