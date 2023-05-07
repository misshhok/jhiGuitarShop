import { IUser } from 'app/entities/user/user.model';
import { IGuitar } from 'app/entities/guitar/guitar.model';

export interface IGuitarOrder {
  id: number;
  totalPrice?: number | null;
  orderOwner?: Pick<IUser, 'id' | 'login'> | null;
  guitarsInOrders?: Pick<IGuitar, 'id'>[] | null;
}

export type NewGuitarOrder = Omit<IGuitarOrder, 'id'> & { id: null };
