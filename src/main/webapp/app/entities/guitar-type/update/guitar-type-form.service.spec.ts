import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../guitar-type.test-samples';

import { GuitarTypeFormService } from './guitar-type-form.service';

describe('GuitarType Form Service', () => {
  let service: GuitarTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GuitarTypeFormService);
  });

  describe('Service methods', () => {
    describe('createGuitarTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGuitarTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
          })
        );
      });

      it('passing IGuitarType should create a new form with FormGroup', () => {
        const formGroup = service.createGuitarTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
          })
        );
      });
    });

    describe('getGuitarType', () => {
      it('should return NewGuitarType for default GuitarType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGuitarTypeFormGroup(sampleWithNewData);

        const guitarType = service.getGuitarType(formGroup) as any;

        expect(guitarType).toMatchObject(sampleWithNewData);
      });

      it('should return NewGuitarType for empty GuitarType initial value', () => {
        const formGroup = service.createGuitarTypeFormGroup();

        const guitarType = service.getGuitarType(formGroup) as any;

        expect(guitarType).toMatchObject({});
      });

      it('should return IGuitarType', () => {
        const formGroup = service.createGuitarTypeFormGroup(sampleWithRequiredData);

        const guitarType = service.getGuitarType(formGroup) as any;

        expect(guitarType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGuitarType should not enable id FormControl', () => {
        const formGroup = service.createGuitarTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGuitarType should disable id FormControl', () => {
        const formGroup = service.createGuitarTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
