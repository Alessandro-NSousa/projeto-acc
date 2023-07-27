import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITurmaACC } from '../turma-acc.model';
import { TurmaACCService } from '../service/turma-acc.service';
import { TurmaACCDeleteDialogComponent } from '../delete/turma-acc-delete-dialog.component';

@Component({
  selector: 'jhi-turma-acc',
  templateUrl: './turma-acc.component.html',
})
export class TurmaACCComponent implements OnInit {
  turmaACCS?: ITurmaACC[];
  isLoading = false;

  constructor(protected turmaACCService: TurmaACCService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.turmaACCService.query().subscribe({
      next: (res: HttpResponse<ITurmaACC[]>) => {
        this.isLoading = false;
        this.turmaACCS = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITurmaACC): number {
    return item.id!;
  }

  delete(turmaACC: ITurmaACC): void {
    const modalRef = this.modalService.open(TurmaACCDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.turmaACC = turmaACC;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
