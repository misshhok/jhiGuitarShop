import { IGuitar, NewGuitar } from './guitar.model';

export const sampleWithRequiredData: IGuitar = {
  id: 47325,
};

export const sampleWithPartialData: IGuitar = {
  id: 31479,
  title: 'Свободный',
  price: 1170,
};

export const sampleWithFullData: IGuitar = {
  id: 44885,
  title: 'маркетинговый коммуникационный вычислить',
  price: 44608,
};

export const sampleWithNewData: NewGuitar = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
