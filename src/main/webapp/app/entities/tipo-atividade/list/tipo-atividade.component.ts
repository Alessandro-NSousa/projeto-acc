import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoAtividade } from '../tipo-atividade.model';
import { TipoAtividadeService } from '../service/tipo-atividade.service';
import { TipoAtividadeDeleteDialogComponent } from '../delete/tipo-atividade-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tipo-atividade',
  templateUrl: './tipo-atividade.component.html',
})
export class TipoAtividadeComponent implements OnInit {
  tipoAtividades?: ITipoAtividade[];
  isLoading = false;

  constructor(protected tipoAtividadeService: TipoAtividadeService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tipoAtividadeService.query().subscribe({
      next: (res: HttpResponse<ITipoAtividade[]>) => {
        this.isLoading = false;
        this.tipoAtividades = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITipoAtividade): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(tipoAtividade: ITipoAtividade): void {
    const modalRef = this.modalService.open(TipoAtividadeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tipoAtividade = tipoAtividade;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
