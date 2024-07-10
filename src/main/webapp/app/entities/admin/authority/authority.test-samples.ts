import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'b8e0b039-6c64-4908-9417-65954052e853',
};

export const sampleWithPartialData: IAuthority = {
  name: '6fbefef2-f63a-4b4e-a6c0-3b01bd1fbb64',
};

export const sampleWithFullData: IAuthority = {
  name: 'd5cf91df-6401-479e-8440-4bf7a12a22dc',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
