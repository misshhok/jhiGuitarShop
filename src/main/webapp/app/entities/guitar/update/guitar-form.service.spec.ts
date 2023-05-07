import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../guitar.test-samples';

import { GuitarFormService } from './guitar-form.service';

describe('Guitar Form Service', () => {
  let service: GuitarFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GuitarFormService);
  });

  describe('Service methods', () => {
    describe('createGuitarFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGuitarFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            price: expect.any(Object),
            guitarType: expect.any(Object),
          })
        );
      });

      it('passing IGuitar should create a new form with FormGroup', () => {
        const formGroup = service.createGuitarFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            price: expect.any(Object),
            guitarType: expect.any(Object),
          })
        );
      });
    });

    describe('getGuitar', () => {
      it('should return NewGuitar for default Guitar initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGuitarFormGroup(sampleWithNewData);

        const guitar = service.getGuitar(formGroup) as any;

        expect(guitar).toMatchObject(sampleWithNewData);
      });

      it('should return NewGuitar for empty Guitar initial value', () => {
        const formGroup = service.createGuitarFormGroup();

        const guitar = service.getGuitar(formGroup) as any;

        expect(guitar).toMatchObject({});
      });

      it('should return IGuitar', () => {
        const formGroup = service.createGuitarFormGroup(sampleWithRequiredData);

        const guitar = service.getGuitar(formGroup) as any;

        expect(guitar).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGuitar should not enable id FormControl', () => {
        const formGroup = service.createGuitarFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGuitar should disable id FormControl', () => {
        const formGroup = service.createGuitarFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
