import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGuitar, NewGuitar } from '../guitar.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGuitar for edit and NewGuitarFormGroupInput for create.
 */
type GuitarFormGroupInput = IGuitar | PartialWithRequiredKeyOf<NewGuitar>;

type GuitarFormDefaults = Pick<NewGuitar, 'id'>;

type GuitarFormGroupContent = {
  id: FormControl<IGuitar['id'] | NewGuitar['id']>;
  title: FormControl<IGuitar['title']>;
  price: FormControl<IGuitar['price']>;
  guitarType: FormControl<IGuitar['guitarType']>;
};

export type GuitarFormGroup = FormGroup<GuitarFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GuitarFormService {
  createGuitarFormGroup(guitar: GuitarFormGroupInput = { id: null }): GuitarFormGroup {
    const guitarRawValue = {
      ...this.getFormDefaults(),
      ...guitar,
    };
    return new FormGroup<GuitarFormGroupContent>({
      id: new FormControl(
        { value: guitarRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(guitarRawValue.title),
      price: new FormControl(guitarRawValue.price),
      guitarType: new FormControl(guitarRawValue.guitarType),
    });
  }

  getGuitar(form: GuitarFormGroup): IGuitar | NewGuitar {
    return form.getRawValue() as IGuitar | NewGuitar;
  }

  resetForm(form: GuitarFormGroup, guitar: GuitarFormGroupInput): void {
    const guitarRawValue = { ...this.getFormDefaults(), ...guitar };
    form.reset(
      {
        ...guitarRawValue,
        id: { value: guitarRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GuitarFormDefaults {
    return {
      id: null,
    };
  }
}
