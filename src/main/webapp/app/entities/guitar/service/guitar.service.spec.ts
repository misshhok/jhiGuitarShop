import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGuitar } from '../guitar.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../guitar.test-samples';

import { GuitarService } from './guitar.service';

const requireRestSample: IGuitar = {
  ...sampleWithRequiredData,
};

describe('Guitar Service', () => {
  let service: GuitarService;
  let httpMock: HttpTestingController;
  let expectedResult: IGuitar | IGuitar[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GuitarService);
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

    it('should create a Guitar', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const guitar = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(guitar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Guitar', () => {
      const guitar = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(guitar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Guitar', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Guitar', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Guitar', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGuitarToCollectionIfMissing', () => {
      it('should add a Guitar to an empty array', () => {
        const guitar: IGuitar = sampleWithRequiredData;
        expectedResult = service.addGuitarToCollectionIfMissing([], guitar);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(guitar);
      });

      it('should not add a Guitar to an array that contains it', () => {
        const guitar: IGuitar = sampleWithRequiredData;
        const guitarCollection: IGuitar[] = [
          {
            ...guitar,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGuitarToCollectionIfMissing(guitarCollection, guitar);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Guitar to an array that doesn't contain it", () => {
        const guitar: IGuitar = sampleWithRequiredData;
        const guitarCollection: IGuitar[] = [sampleWithPartialData];
        expectedResult = service.addGuitarToCollectionIfMissing(guitarCollection, guitar);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(guitar);
      });

      it('should add only unique Guitar to an array', () => {
        const guitarArray: IGuitar[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const guitarCollection: IGuitar[] = [sampleWithRequiredData];
        expectedResult = service.addGuitarToCollectionIfMissing(guitarCollection, ...guitarArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const guitar: IGuitar = sampleWithRequiredData;
        const guitar2: IGuitar = sampleWithPartialData;
        expectedResult = service.addGuitarToCollectionIfMissing([], guitar, guitar2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(guitar);
        expect(expectedResult).toContain(guitar2);
      });

      it('should accept null and undefined values', () => {
        const guitar: IGuitar = sampleWithRequiredData;
        expectedResult = service.addGuitarToCollectionIfMissing([], null, guitar, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(guitar);
      });

      it('should return initial array if no Guitar is added', () => {
        const guitarCollection: IGuitar[] = [sampleWithRequiredData];
        expectedResult = service.addGuitarToCollectionIfMissing(guitarCollection, undefined, null);
        expect(expectedResult).toEqual(guitarCollection);
      });
    });

    describe('compareGuitar', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGuitar(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGuitar(entity1, entity2);
        const compareResult2 = service.compareGuitar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGuitar(entity1, entity2);
        const compareResult2 = service.compareGuitar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGuitar(entity1, entity2);
        const compareResult2 = service.compareGuitar(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
