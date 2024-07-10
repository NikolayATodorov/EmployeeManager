import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmployeeProject } from '../employee-project.model';
import { EmployeeProjectService } from '../service/employee-project.service';

@Component({
  standalone: true,
  templateUrl: './employee-project-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmployeeProjectDeleteDialogComponent {
  employeeProject?: IEmployeeProject;

  protected employeeProjectService = inject(EmployeeProjectService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeProjectService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
