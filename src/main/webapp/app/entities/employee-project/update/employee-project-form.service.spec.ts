import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-project.test-samples';

import { EmployeeProjectFormService } from './employee-project-form.service';

describe('EmployeeProject Form Service', () => {
  let service: EmployeeProjectFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeProjectFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeProjectFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeProjectFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateFrom: expect.any(Object),
            dateTo: expect.any(Object),
            employee: expect.any(Object),
            project: expect.any(Object),
          }),
        );
      });

      it('passing IEmployeeProject should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeProjectFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateFrom: expect.any(Object),
            dateTo: expect.any(Object),
            employee: expect.any(Object),
            project: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmployeeProject', () => {
      it('should return NewEmployeeProject for default EmployeeProject initial value', () => {
        const formGroup = service.createEmployeeProjectFormGroup(sampleWithNewData);

        const employeeProject = service.getEmployeeProject(formGroup) as any;

        expect(employeeProject).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeProject for empty EmployeeProject initial value', () => {
        const formGroup = service.createEmployeeProjectFormGroup();

        const employeeProject = service.getEmployeeProject(formGroup) as any;

        expect(employeeProject).toMatchObject({});
      });

      it('should return IEmployeeProject', () => {
        const formGroup = service.createEmployeeProjectFormGroup(sampleWithRequiredData);

        const employeeProject = service.getEmployeeProject(formGroup) as any;

        expect(employeeProject).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeProject should not enable id FormControl', () => {
        const formGroup = service.createEmployeeProjectFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeProject should disable id FormControl', () => {
        const formGroup = service.createEmployeeProjectFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
