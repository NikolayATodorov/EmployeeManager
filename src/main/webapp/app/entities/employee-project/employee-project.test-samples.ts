import dayjs from 'dayjs/esm';

import { IEmployeeProject, NewEmployeeProject } from './employee-project.model';

export const sampleWithRequiredData: IEmployeeProject = {
  id: 12385,
  dateFrom: dayjs('2024-07-10T06:11'),
  dateTo: dayjs('2024-07-09T21:49'),
};

export const sampleWithPartialData: IEmployeeProject = {
  id: 9141,
  dateFrom: dayjs('2024-07-09T13:00'),
  dateTo: dayjs('2024-07-10T00:58'),
};

export const sampleWithFullData: IEmployeeProject = {
  id: 20386,
  dateFrom: dayjs('2024-07-09T14:42'),
  dateTo: dayjs('2024-07-10T10:56'),
};

export const sampleWithNewData: NewEmployeeProject = {
  dateFrom: dayjs('2024-07-10T08:16'),
  dateTo: dayjs('2024-07-09T17:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
