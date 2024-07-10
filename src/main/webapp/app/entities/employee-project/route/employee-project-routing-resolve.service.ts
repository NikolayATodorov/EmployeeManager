import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeProject } from '../employee-project.model';
import { EmployeeProjectService } from '../service/employee-project.service';

const employeeProjectResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmployeeProject> => {
  const id = route.params['id'];
  if (id) {
    return inject(EmployeeProjectService)
      .find(id)
      .pipe(
        mergeMap((employeeProject: HttpResponse<IEmployeeProject>) => {
          if (employeeProject.body) {
            return of(employeeProject.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default employeeProjectResolve;
