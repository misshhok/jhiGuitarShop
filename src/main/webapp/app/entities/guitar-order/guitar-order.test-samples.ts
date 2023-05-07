import { IGuitarOrder, NewGuitarOrder } from './guitar-order.model';

export const sampleWithRequiredData: IGuitarOrder = {
  id: 15458,
};

export const sampleWithPartialData: IGuitarOrder = {
  id: 92427,
  totalPrice: 81545,
};

export const sampleWithFullData: IGuitarOrder = {
  id: 95005,
  totalPrice: 7857,
};

export const sampleWithNewData: NewGuitarOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
