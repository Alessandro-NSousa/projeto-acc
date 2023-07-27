import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITipoAtividade, TipoAtividade } from '../tipo-atividade.model';
import { TipoAtividadeService } from '../service/tipo-atividade.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tipo-atividade-update',
  templateUrl: './tipo-atividade-update.component.html',
})
export class TipoAtividadeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    descricao: [],
    numeroPontos: [],
    dataCriacao: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tipoAtividadeService: TipoAtividadeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoAtividade }) => {
      if (tipoAtividade.id === undefined) {
        const today = dayjs().startOf('day');
        tipoAtividade.dataCriacao = today;
      }

      this.updateForm(tipoAtividade);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('controleDeAccApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoAtividade = this.createFromForm();
    if (tipoAtividade.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoAtividadeService.update(tipoAtividade));
    } else {
      this.subscribeToSaveResponse(this.tipoAtividadeService.create(tipoAtividade));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoAtividade>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tipoAtividade: ITipoAtividade): void {
    this.editForm.patchValue({
      id: tipoAtividade.id,
      nome: tipoAtividade.nome,
      descricao: tipoAtividade.descricao,
      numeroPontos: tipoAtividade.numeroPontos,
      dataCriacao: tipoAtividade.dataCriacao ? tipoAtividade.dataCriacao.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ITipoAtividade {
    return {
      ...new TipoAtividade(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      numeroPontos: this.editForm.get(['numeroPontos'])!.value,
      dataCriacao: this.editForm.get(['dataCriacao'])!.value
        ? dayjs(this.editForm.get(['dataCriacao'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
