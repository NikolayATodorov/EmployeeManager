import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployeeProject } from '../employee-project.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employee-project.test-samples';

import { EmployeeProjectService, RestEmployeeProject } from './employee-project.service';

const requireRestSample: RestEmployeeProject = {
  ...sampleWithRequiredData,
  dateFrom: sampleWithRequiredData.dateFrom?.toJSON(),
  dateTo: sampleWithRequiredData.dateTo?.toJSON(),
};

describe('EmployeeProject Service', () => {
  let service: EmployeeProjectService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeeProject | IEmployeeProject[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeProjectService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a EmployeeProject', () => {
      const employeeProject = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeeProject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeProject', () => {
      const employeeProject = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeeProject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeProject', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeProject', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeeProject', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeeProjectToCollectionIfMissing', () => {
      it('should add a EmployeeProject to an empty array', () => {
        const employeeProject: IEmployeeProject = sampleWithRequiredData;
        expectedResult = service.addEmployeeProjectToCollectionIfMissing([], employeeProject);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeProject);
      });

      it('should not add a EmployeeProject to an array that contains it', () => {
        const employeeProject: IEmployeeProject = sampleWithRequiredData;
        const employeeProjectCollection: IEmployeeProject[] = [
          {
            ...employeeProject,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeeProjectToCollectionIfMissing(employeeProjectCollection, employeeProject);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeProject to an array that doesn't contain it", () => {
        const employeeProject: IEmployeeProject = sampleWithRequiredData;
        const employeeProjectCollection: IEmployeeProject[] = [sampleWithPartialData];
        expectedResult = service.addEmployeeProjectToCollectionIfMissing(employeeProjectCollection, employeeProject);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeProject);
      });

      it('should add only unique EmployeeProject to an array', () => {
        const employeeProjectArray: IEmployeeProject[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeeProjectCollection: IEmployeeProject[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeProjectToCollectionIfMissing(employeeProjectCollection, ...employeeProjectArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeProject: IEmployeeProject = sampleWithRequiredData;
        const employeeProject2: IEmployeeProject = sampleWithPartialData;
        expectedResult = service.addEmployeeProjectToCollectionIfMissing([], employeeProject, employeeProject2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeProject);
        expect(expectedResult).toContain(employeeProject2);
      });

      it('should accept null and undefined values', () => {
        const employeeProject: IEmployeeProject = sampleWithRequiredData;
        expectedResult = service.addEmployeeProjectToCollectionIfMissing([], null, employeeProject, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeProject);
      });

      it('should return initial array if no EmployeeProject is added', () => {
        const employeeProjectCollection: IEmployeeProject[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeProjectToCollectionIfMissing(employeeProjectCollection, undefined, null);
        expect(expectedResult).toEqual(employeeProjectCollection);
      });
    });

    describe('compareEmployeeProject', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeeProject(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeeProject(entity1, entity2);
        const compareResult2 = service.compareEmployeeProject(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeeProject(entity1, entity2);
        const compareResult2 = service.compareEmployeeProject(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeeProject(entity1, entity2);
        const compareResult2 = service.compareEmployeeProject(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
