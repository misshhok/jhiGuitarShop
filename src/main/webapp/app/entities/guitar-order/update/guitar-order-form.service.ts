import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGuitarOrder, NewGuitarOrder } from '../guitar-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGuitarOrder for edit and NewGuitarOrderFormGroupInput for create.
 */
type GuitarOrderFormGroupInput = IGuitarOrder | PartialWithRequiredKeyOf<NewGuitarOrder>;

type GuitarOrderFormDefaults = Pick<NewGuitarOrder, 'id' | 'guitarsInOrders'>;

type GuitarOrderFormGroupContent = {
  id: FormControl<IGuitarOrder['id'] | NewGuitarOrder['id']>;
  totalPrice: FormControl<IGuitarOrder['totalPrice']>;
  orderOwner: FormControl<IGuitarOrder['orderOwner']>;
  guitarsInOrders: FormControl<IGuitarOrder['guitarsInOrders']>;
};

export type GuitarOrderFormGroup = FormGroup<GuitarOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GuitarOrderFormService {
  createGuitarOrderFormGroup(guitarOrder: GuitarOrderFormGroupInput = { id: null }): GuitarOrderFormGroup {
    const guitarOrderRawValue = {
      ...this.getFormDefaults(),
      ...guitarOrder,
    };
    return new FormGroup<GuitarOrderFormGroupContent>({
      id: new FormControl(
        { value: guitarOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      totalPrice: new FormControl(guitarOrderRawValue.totalPrice),
      orderOwner: new FormControl(guitarOrderRawValue.orderOwner),
      guitarsInOrders: new FormControl(guitarOrderRawValue.guitarsInOrders ?? []),
    });
  }

  getGuitarOrder(form: GuitarOrderFormGroup): IGuitarOrder | NewGuitarOrder {
    return form.getRawValue() as IGuitarOrder | NewGuitarOrder;
  }

  resetForm(form: GuitarOrderFormGroup, guitarOrder: GuitarOrderFormGroupInput): void {
    const guitarOrderRawValue = { ...this.getFormDefaults(), ...guitarOrder };
    form.reset(
      {
        ...guitarOrderRawValue,
        id: { value: guitarOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GuitarOrderFormDefaults {
    return {
      id: null,
      guitarsInOrders: [],
    };
  }
}
