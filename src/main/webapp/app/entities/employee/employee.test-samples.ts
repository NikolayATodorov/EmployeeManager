import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 26478,
  firstName: 'Alexandre',
  lastName: 'Bruen',
};

export const sampleWithPartialData: IEmployee = {
  id: 29509,
  firstName: 'Maiya',
  lastName: 'Davis',
};

export const sampleWithFullData: IEmployee = {
  id: 21685,
  firstName: 'Amir',
  lastName: 'Murazik',
};

export const sampleWithNewData: NewEmployee = {
  firstName: 'Tabitha',
  lastName: 'Cummerata',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
