import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoAtividade } from '../tipo-atividade.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tipo-atividade-detail',
  templateUrl: './tipo-atividade-detail.component.html',
})
export class TipoAtividadeDetailComponent implements OnInit {
  tipoAtividade: ITipoAtividade | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoAtividade }) => {
      this.tipoAtividade = tipoAtividade;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
