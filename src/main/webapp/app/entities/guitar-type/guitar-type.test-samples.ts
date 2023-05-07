import { IGuitarType, NewGuitarType } from './guitar-type.model';

export const sampleWithRequiredData: IGuitarType = {
  id: 74896,
};

export const sampleWithPartialData: IGuitarType = {
  id: 84301,
};

export const sampleWithFullData: IGuitarType = {
  id: 92096,
  title: 'Дания',
};

export const sampleWithNewData: NewGuitarType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
