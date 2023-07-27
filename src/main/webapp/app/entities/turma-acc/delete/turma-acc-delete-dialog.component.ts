import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITurmaACC } from '../turma-acc.model';
import { TurmaACCService } from '../service/turma-acc.service';

@Component({
  templateUrl: './turma-acc-delete-dialog.component.html',
})
export class TurmaACCDeleteDialogComponent {
  turmaACC?: ITurmaACC;

  constructor(protected turmaACCService: TurmaACCService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.turmaACCService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
