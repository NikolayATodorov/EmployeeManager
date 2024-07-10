import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployeeProject, NewEmployeeProject } from '../employee-project.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeProject for edit and NewEmployeeProjectFormGroupInput for create.
 */
type EmployeeProjectFormGroupInput = IEmployeeProject | PartialWithRequiredKeyOf<NewEmployeeProject>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployeeProject | NewEmployeeProject> = Omit<T, 'dateFrom' | 'dateTo'> & {
  dateFrom?: string | null;
  dateTo?: string | null;
};

type EmployeeProjectFormRawValue = FormValueOf<IEmployeeProject>;

type NewEmployeeProjectFormRawValue = FormValueOf<NewEmployeeProject>;

type EmployeeProjectFormDefaults = Pick<NewEmployeeProject, 'id' | 'dateFrom' | 'dateTo'>;

type EmployeeProjectFormGroupContent = {
  id: FormControl<EmployeeProjectFormRawValue['id'] | NewEmployeeProject['id']>;
  dateFrom: FormControl<EmployeeProjectFormRawValue['dateFrom']>;
  dateTo: FormControl<EmployeeProjectFormRawValue['dateTo']>;
  employee: FormControl<EmployeeProjectFormRawValue['employee']>;
  project: FormControl<EmployeeProjectFormRawValue['project']>;
};

export type EmployeeProjectFormGroup = FormGroup<EmployeeProjectFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeProjectFormService {
  createEmployeeProjectFormGroup(employeeProject: EmployeeProjectFormGroupInput = { id: null }): EmployeeProjectFormGroup {
    const employeeProjectRawValue = this.convertEmployeeProjectToEmployeeProjectRawValue({
      ...this.getFormDefaults(),
      ...employeeProject,
    });
    return new FormGroup<EmployeeProjectFormGroupContent>({
      id: new FormControl(
        { value: employeeProjectRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateFrom: new FormControl(employeeProjectRawValue.dateFrom, {
        validators: [Validators.required],
      }),
      dateTo: new FormControl(employeeProjectRawValue.dateTo, {
        validators: [Validators.required],
      }),
      employee: new FormControl(employeeProjectRawValue.employee, {
        validators: [Validators.required],
      }),
      project: new FormControl(employeeProjectRawValue.project, {
        validators: [Validators.required],
      }),
    });
  }

  getEmployeeProject(form: EmployeeProjectFormGroup): IEmployeeProject | NewEmployeeProject {
    return this.convertEmployeeProjectRawValueToEmployeeProject(
      form.getRawValue() as EmployeeProjectFormRawValue | NewEmployeeProjectFormRawValue,
    );
  }

  resetForm(form: EmployeeProjectFormGroup, employeeProject: EmployeeProjectFormGroupInput): void {
    const employeeProjectRawValue = this.convertEmployeeProjectToEmployeeProjectRawValue({ ...this.getFormDefaults(), ...employeeProject });
    form.reset(
      {
        ...employeeProjectRawValue,
        id: { value: employeeProjectRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmployeeProjectFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateFrom: currentTime,
      dateTo: currentTime,
    };
  }

  private convertEmployeeProjectRawValueToEmployeeProject(
    rawEmployeeProject: EmployeeProjectFormRawValue | NewEmployeeProjectFormRawValue,
  ): IEmployeeProject | NewEmployeeProject {
    return {
      ...rawEmployeeProject,
      dateFrom: dayjs(rawEmployeeProject.dateFrom, DATE_TIME_FORMAT),
      dateTo: dayjs(rawEmployeeProject.dateTo, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeeProjectToEmployeeProjectRawValue(
    employeeProject: IEmployeeProject | (Partial<NewEmployeeProject> & EmployeeProjectFormDefaults),
  ): EmployeeProjectFormRawValue | PartialWithRequiredKeyOf<NewEmployeeProjectFormRawValue> {
    return {
      ...employeeProject,
      dateFrom: employeeProject.dateFrom ? employeeProject.dateFrom.format(DATE_TIME_FORMAT) : undefined,
      dateTo: employeeProject.dateTo ? employeeProject.dateTo.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
