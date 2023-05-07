import { IGuitarType } from 'app/entities/guitar-type/guitar-type.model';

export interface IGuitar {
  id: number;
  title?: string | null;
  price?: number | null;
  guitarType?: Pick<IGuitarType, 'id'> | null;
}

export type NewGuitar = Omit<IGuitar, 'id'> & { id: null };
