import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITurmaACC } from '../turma-acc.model';

@Component({
  selector: 'jhi-turma-acc-detail',
  templateUrl: './turma-acc-detail.component.html',
})
export class TurmaACCDetailComponent implements OnInit {
  turmaACC: ITurmaACC | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ turmaACC }) => {
      this.turmaACC = turmaACC;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
