import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGuitarOrder } from '../guitar-order.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../guitar-order.test-samples';

import { GuitarOrderService } from './guitar-order.service';

const requireRestSample: IGuitarOrder = {
  ...sampleWithRequiredData,
};

describe('GuitarOrder Service', () => {
  let service: GuitarOrderService;
  let httpMock: HttpTestingController;
  let expectedResult: IGuitarOrder | IGuitarOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GuitarOrderService);
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

    it('should create a GuitarOrder', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const guitarOrder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(guitarOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GuitarOrder', () => {
      const guitarOrder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(guitarOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GuitarOrder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GuitarOrder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GuitarOrder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGuitarOrderToCollectionIfMissing', () => {
      it('should add a GuitarOrder to an empty array', () => {
        const guitarOrder: IGuitarOrder = sampleWithRequiredData;
        expectedResult = service.addGuitarOrderToCollectionIfMissing([], guitarOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(guitarOrder);
      });

      it('should not add a GuitarOrder to an array that contains it', () => {
        const guitarOrder: IGuitarOrder = sampleWithRequiredData;
        const guitarOrderCollection: IGuitarOrder[] = [
          {
            ...guitarOrder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGuitarOrderToCollectionIfMissing(guitarOrderCollection, guitarOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GuitarOrder to an array that doesn't contain it", () => {
        const guitarOrder: IGuitarOrder = sampleWithRequiredData;
        const guitarOrderCollection: IGuitarOrder[] = [sampleWithPartialData];
        expectedResult = service.addGuitarOrderToCollectionIfMissing(guitarOrderCollection, guitarOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(guitarOrder);
      });

      it('should add only unique GuitarOrder to an array', () => {
        const guitarOrderArray: IGuitarOrder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const guitarOrderCollection: IGuitarOrder[] = [sampleWithRequiredData];
        expectedResult = service.addGuitarOrderToCollectionIfMissing(guitarOrderCollection, ...guitarOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const guitarOrder: IGuitarOrder = sampleWithRequiredData;
        const guitarOrder2: IGuitarOrder = sampleWithPartialData;
        expectedResult = service.addGuitarOrderToCollectionIfMissing([], guitarOrder, guitarOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(guitarOrder);
        expect(expectedResult).toContain(guitarOrder2);
      });

      it('should accept null and undefined values', () => {
        const guitarOrder: IGuitarOrder = sampleWithRequiredData;
        expectedResult = service.addGuitarOrderToCollectionIfMissing([], null, guitarOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(guitarOrder);
      });

      it('should return initial array if no GuitarOrder is added', () => {
        const guitarOrderCollection: IGuitarOrder[] = [sampleWithRequiredData];
        expectedResult = service.addGuitarOrderToCollectionIfMissing(guitarOrderCollection, undefined, null);
        expect(expectedResult).toEqual(guitarOrderCollection);
      });
    });

    describe('compareGuitarOrder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGuitarOrder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGuitarOrder(entity1, entity2);
        const compareResult2 = service.compareGuitarOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGuitarOrder(entity1, entity2);
        const compareResult2 = service.compareGuitarOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGuitarOrder(entity1, entity2);
        const compareResult2 = service.compareGuitarOrder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
