import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 30089,
  name: 'vastly acidly',
};

export const sampleWithPartialData: IProject = {
  id: 30361,
  name: 'gee nor pro',
};

export const sampleWithFullData: IProject = {
  id: 15588,
  name: 'since burly',
};

export const sampleWithNewData: NewProject = {
  name: 'awkwardly bargain',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
