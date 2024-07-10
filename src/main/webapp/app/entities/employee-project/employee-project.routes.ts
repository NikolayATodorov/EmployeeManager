import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EmployeeProjectComponent } from './list/employee-project.component';
import { EmployeeProjectDetailComponent } from './detail/employee-project-detail.component';
import { EmployeeProjectUpdateComponent } from './update/employee-project-update.component';
import EmployeeProjectResolve from './route/employee-project-routing-resolve.service';

const employeeProjectRoute: Routes = [
  {
    path: '',
    component: EmployeeProjectComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeProjectDetailComponent,
    resolve: {
      employeeProject: EmployeeProjectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeProjectUpdateComponent,
    resolve: {
      employeeProject: EmployeeProjectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeProjectUpdateComponent,
    resolve: {
      employeeProject: EmployeeProjectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default employeeProjectRoute;
