import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IProject } from 'app/entities/project/project.model';

export interface IEmployeeProject {
  id: number;
  dateFrom?: dayjs.Dayjs | null;
  dateTo?: dayjs.Dayjs | null;
  employee?: Pick<IEmployee, 'id'> | null;
  project?: Pick<IProject, 'id'> | null;
}

export type NewEmployeeProject = Omit<IEmployeeProject, 'id'> & { id: null };
