import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../guitar-order.test-samples';

import { GuitarOrderFormService } from './guitar-order-form.service';

describe('GuitarOrder Form Service', () => {
  let service: GuitarOrderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GuitarOrderFormService);
  });

  describe('Service methods', () => {
    describe('createGuitarOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGuitarOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            totalPrice: expect.any(Object),
            orderOwner: expect.any(Object),
            guitarsInOrders: expect.any(Object),
          })
        );
      });

      it('passing IGuitarOrder should create a new form with FormGroup', () => {
        const formGroup = service.createGuitarOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            totalPrice: expect.any(Object),
            orderOwner: expect.any(Object),
            guitarsInOrders: expect.any(Object),
          })
        );
      });
    });

    describe('getGuitarOrder', () => {
      it('should return NewGuitarOrder for default GuitarOrder initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGuitarOrderFormGroup(sampleWithNewData);

        const guitarOrder = service.getGuitarOrder(formGroup) as any;

        expect(guitarOrder).toMatchObject(sampleWithNewData);
      });

      it('should return NewGuitarOrder for empty GuitarOrder initial value', () => {
        const formGroup = service.createGuitarOrderFormGroup();

        const guitarOrder = service.getGuitarOrder(formGroup) as any;

        expect(guitarOrder).toMatchObject({});
      });

      it('should return IGuitarOrder', () => {
        const formGroup = service.createGuitarOrderFormGroup(sampleWithRequiredData);

        const guitarOrder = service.getGuitarOrder(formGroup) as any;

        expect(guitarOrder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGuitarOrder should not enable id FormControl', () => {
        const formGroup = service.createGuitarOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGuitarOrder should disable id FormControl', () => {
        const formGroup = service.createGuitarOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
