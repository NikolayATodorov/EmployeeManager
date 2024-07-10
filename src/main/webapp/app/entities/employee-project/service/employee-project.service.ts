import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeProject, NewEmployeeProject } from '../employee-project.model';

export type PartialUpdateEmployeeProject = Partial<IEmployeeProject> & Pick<IEmployeeProject, 'id'>;

type RestOf<T extends IEmployeeProject | NewEmployeeProject> = Omit<T, 'dateFrom' | 'dateTo'> & {
  dateFrom?: string | null;
  dateTo?: string | null;
};

export type RestEmployeeProject = RestOf<IEmployeeProject>;

export type NewRestEmployeeProject = RestOf<NewEmployeeProject>;

export type PartialUpdateRestEmployeeProject = RestOf<PartialUpdateEmployeeProject>;

export type EntityResponseType = HttpResponse<IEmployeeProject>;
export type EntityArrayResponseType = HttpResponse<IEmployeeProject[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeProjectService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-projects');

  create(employeeProject: NewEmployeeProject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeProject);
    return this.http
      .post<RestEmployeeProject>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employeeProject: IEmployeeProject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeProject);
    return this.http
      .put<RestEmployeeProject>(`${this.resourceUrl}/${this.getEmployeeProjectIdentifier(employeeProject)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employeeProject: PartialUpdateEmployeeProject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeProject);
    return this.http
      .patch<RestEmployeeProject>(`${this.resourceUrl}/${this.getEmployeeProjectIdentifier(employeeProject)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployeeProject>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployeeProject[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeProjectIdentifier(employeeProject: Pick<IEmployeeProject, 'id'>): number {
    return employeeProject.id;
  }

  compareEmployeeProject(o1: Pick<IEmployeeProject, 'id'> | null, o2: Pick<IEmployeeProject, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeProjectIdentifier(o1) === this.getEmployeeProjectIdentifier(o2) : o1 === o2;
  }

  addEmployeeProjectToCollectionIfMissing<Type extends Pick<IEmployeeProject, 'id'>>(
    employeeProjectCollection: Type[],
    ...employeeProjectsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeProjects: Type[] = employeeProjectsToCheck.filter(isPresent);
    if (employeeProjects.length > 0) {
      const employeeProjectCollectionIdentifiers = employeeProjectCollection.map(employeeProjectItem =>
        this.getEmployeeProjectIdentifier(employeeProjectItem),
      );
      const employeeProjectsToAdd = employeeProjects.filter(employeeProjectItem => {
        const employeeProjectIdentifier = this.getEmployeeProjectIdentifier(employeeProjectItem);
        if (employeeProjectCollectionIdentifiers.includes(employeeProjectIdentifier)) {
          return false;
        }
        employeeProjectCollectionIdentifiers.push(employeeProjectIdentifier);
        return true;
      });
      return [...employeeProjectsToAdd, ...employeeProjectCollection];
    }
    return employeeProjectCollection;
  }

  protected convertDateFromClient<T extends IEmployeeProject | NewEmployeeProject | PartialUpdateEmployeeProject>(
    employeeProject: T,
  ): RestOf<T> {
    return {
      ...employeeProject,
      dateFrom: employeeProject.dateFrom?.toJSON() ?? null,
      dateTo: employeeProject.dateTo?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmployeeProject: RestEmployeeProject): IEmployeeProject {
    return {
      ...restEmployeeProject,
      dateFrom: restEmployeeProject.dateFrom ? dayjs(restEmployeeProject.dateFrom) : undefined,
      dateTo: restEmployeeProject.dateTo ? dayjs(restEmployeeProject.dateTo) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmployeeProject>): HttpResponse<IEmployeeProject> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmployeeProject[]>): HttpResponse<IEmployeeProject[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
