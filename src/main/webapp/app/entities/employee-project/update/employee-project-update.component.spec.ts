import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IEmployeeProject } from '../employee-project.model';
import { EmployeeProjectService } from '../service/employee-project.service';
import { EmployeeProjectFormService } from './employee-project-form.service';

import { EmployeeProjectUpdateComponent } from './employee-project-update.component';

describe('EmployeeProject Management Update Component', () => {
  let comp: EmployeeProjectUpdateComponent;
  let fixture: ComponentFixture<EmployeeProjectUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeProjectFormService: EmployeeProjectFormService;
  let employeeProjectService: EmployeeProjectService;
  let employeeService: EmployeeService;
  let projectService: ProjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, EmployeeProjectUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmployeeProjectUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeProjectUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeProjectFormService = TestBed.inject(EmployeeProjectFormService);
    employeeProjectService = TestBed.inject(EmployeeProjectService);
    employeeService = TestBed.inject(EmployeeService);
    projectService = TestBed.inject(ProjectService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const employeeProject: IEmployeeProject = { id: 456 };
      const employee: IEmployee = { id: 19167 };
      employeeProject.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 7893 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeProject });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Project query and add missing value', () => {
      const employeeProject: IEmployeeProject = { id: 456 };
      const project: IProject = { id: 2782 };
      employeeProject.project = project;

      const projectCollection: IProject[] = [{ id: 19353 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const additionalProjects = [project];
      const expectedCollection: IProject[] = [...additionalProjects, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeProject });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(
        projectCollection,
        ...additionalProjects.map(expect.objectContaining),
      );
      expect(comp.projectsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeProject: IEmployeeProject = { id: 456 };
      const employee: IEmployee = { id: 10032 };
      employeeProject.employee = employee;
      const project: IProject = { id: 27763 };
      employeeProject.project = project;

      activatedRoute.data = of({ employeeProject });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.projectsSharedCollection).toContain(project);
      expect(comp.employeeProject).toEqual(employeeProject);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeProject>>();
      const employeeProject = { id: 123 };
      jest.spyOn(employeeProjectFormService, 'getEmployeeProject').mockReturnValue(employeeProject);
      jest.spyOn(employeeProjectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeProject });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeProject }));
      saveSubject.complete();

      // THEN
      expect(employeeProjectFormService.getEmployeeProject).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeProjectService.update).toHaveBeenCalledWith(expect.objectContaining(employeeProject));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeProject>>();
      const employeeProject = { id: 123 };
      jest.spyOn(employeeProjectFormService, 'getEmployeeProject').mockReturnValue({ id: null });
      jest.spyOn(employeeProjectService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeProject: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeProject }));
      saveSubject.complete();

      // THEN
      expect(employeeProjectFormService.getEmployeeProject).toHaveBeenCalled();
      expect(employeeProjectService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeProject>>();
      const employeeProject = { id: 123 };
      jest.spyOn(employeeProjectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeProject });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeProjectService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProject', () => {
      it('Should forward to projectService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(projectService, 'compareProject');
        comp.compareProject(entity, entity2);
        expect(projectService.compareProject).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
