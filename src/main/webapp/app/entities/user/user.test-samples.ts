import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 24359,
  login: '?X@hJ4c\\3rzR\\/WYDmy\\qZ\\RQVssj\\!m',
};

export const sampleWithPartialData: IUser = {
  id: 25735,
  login: '41vPn@Fc\\oFoiii\\-Hv6n\\Lyw8s\\3Ht',
};

export const sampleWithFullData: IUser = {
  id: 4928,
  login: '^uao@OIzu\\]sl2Qi6\\{417iI\\|Ir\\NF',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
