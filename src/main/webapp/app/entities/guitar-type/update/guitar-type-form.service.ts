import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGuitarType, NewGuitarType } from '../guitar-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGuitarType for edit and NewGuitarTypeFormGroupInput for create.
 */
type GuitarTypeFormGroupInput = IGuitarType | PartialWithRequiredKeyOf<NewGuitarType>;

type GuitarTypeFormDefaults = Pick<NewGuitarType, 'id'>;

type GuitarTypeFormGroupContent = {
  id: FormControl<IGuitarType['id'] | NewGuitarType['id']>;
  title: FormControl<IGuitarType['title']>;
};

export type GuitarTypeFormGroup = FormGroup<GuitarTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GuitarTypeFormService {
  createGuitarTypeFormGroup(guitarType: GuitarTypeFormGroupInput = { id: null }): GuitarTypeFormGroup {
    const guitarTypeRawValue = {
      ...this.getFormDefaults(),
      ...guitarType,
    };
    return new FormGroup<GuitarTypeFormGroupContent>({
      id: new FormControl(
        { value: guitarTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(guitarTypeRawValue.title),
    });
  }

  getGuitarType(form: GuitarTypeFormGroup): IGuitarType | NewGuitarType {
    return form.getRawValue() as IGuitarType | NewGuitarType;
  }

  resetForm(form: GuitarTypeFormGroup, guitarType: GuitarTypeFormGroupInput): void {
    const guitarTypeRawValue = { ...this.getFormDefaults(), ...guitarType };
    form.reset(
      {
        ...guitarTypeRawValue,
        id: { value: guitarTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GuitarTypeFormDefaults {
    return {
      id: null,
    };
  }
}
