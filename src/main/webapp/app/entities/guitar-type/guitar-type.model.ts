export interface IGuitarType {
  id: number;
  title?: string | null;
}

export type NewGuitarType = Omit<IGuitarType, 'id'> & { id: null };
