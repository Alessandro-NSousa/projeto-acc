import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICertificado } from '../certificado.model';
import { CertificadoService } from '../service/certificado.service';

@Component({
  templateUrl: './certificado-delete-dialog.component.html',
})
export class CertificadoDeleteDialogComponent {
  certificado?: ICertificado;

  constructor(protected certificadoService: CertificadoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.certificadoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
