import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { EmployeeProjectService } from '../service/employee-project.service';
import { IEmployeeProject } from '../employee-project.model';
import { EmployeeProjectFormService, EmployeeProjectFormGroup } from './employee-project-form.service';

@Component({
  standalone: true,
  selector: 'jhi-employee-project-update',
  templateUrl: './employee-project-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmployeeProjectUpdateComponent implements OnInit {
  isSaving = false;
  employeeProject: IEmployeeProject | null = null;

  employeesSharedCollection: IEmployee[] = [];
  projectsSharedCollection: IProject[] = [];

  protected employeeProjectService = inject(EmployeeProjectService);
  protected employeeProjectFormService = inject(EmployeeProjectFormService);
  protected employeeService = inject(EmployeeService);
  protected projectService = inject(ProjectService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmployeeProjectFormGroup = this.employeeProjectFormService.createEmployeeProjectFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeProject }) => {
      this.employeeProject = employeeProject;
      if (employeeProject) {
        this.updateForm(employeeProject);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeProject = this.employeeProjectFormService.getEmployeeProject(this.editForm);
    if (employeeProject.id !== null) {
      this.subscribeToSaveResponse(this.employeeProjectService.update(employeeProject));
    } else {
      this.subscribeToSaveResponse(this.employeeProjectService.create(employeeProject));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeProject>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(employeeProject: IEmployeeProject): void {
    this.employeeProject = employeeProject;
    this.employeeProjectFormService.resetForm(this.editForm, employeeProject);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      employeeProject.employee,
    );
    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      employeeProject.project,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.employeeProject?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(
        map((projects: IProject[]) =>
          this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.employeeProject?.project),
        ),
      )
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
  }
}
