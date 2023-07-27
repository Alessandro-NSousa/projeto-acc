import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoAtividade } from '../tipo-atividade.model';
import { TipoAtividadeService } from '../service/tipo-atividade.service';

@Component({
  templateUrl: './tipo-atividade-delete-dialog.component.html',
})
export class TipoAtividadeDeleteDialogComponent {
  tipoAtividade?: ITipoAtividade;

  constructor(protected tipoAtividadeService: TipoAtividadeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoAtividadeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
