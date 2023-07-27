import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICertificado, Certificado } from '../certificado.model';
import { CertificadoService } from '../service/certificado.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { ITurmaACC } from 'app/entities/turma-acc/turma-acc.model';
import { TurmaACCService } from 'app/entities/turma-acc/service/turma-acc.service';
import { ITipoAtividade } from 'app/entities/tipo-atividade/tipo-atividade.model';
import { TipoAtividadeService } from 'app/entities/tipo-atividade/service/tipo-atividade.service';
import { Modalidade } from 'app/entities/enumerations/modalidade.model';
import { StatusCertificado } from 'app/entities/enumerations/status-certificado.model';

@Component({
  selector: 'jhi-certificado-update',
  templateUrl: './certificado-update.component.html',
})
export class CertificadoUpdateComponent implements OnInit {
  isSaving = false;
  modalidadeValues = Object.keys(Modalidade);
  statusCertificadoValues = Object.keys(StatusCertificado);

  usuariosSharedCollection: IUsuario[] = [];
  turmaACCSSharedCollection: ITurmaACC[] = [];
  tipoAtividadesSharedCollection: ITipoAtividade[] = [];

  editForm = this.fb.group({
    id: [],
    titulo: [],
    descricao: [],
    dataEnvio: [],
    observacao: [],
    modalidade: [],
    chCuprida: [],
    pontuacao: [],
    status: [],
    caminhoArquivo: [],
    usuario: [],
    turmaAcc: [],
    tipoAtividade: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected certificadoService: CertificadoService,
    protected usuarioService: UsuarioService,
    protected turmaACCService: TurmaACCService,
    protected tipoAtividadeService: TipoAtividadeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ certificado }) => {
      if (certificado.id === undefined) {
        const today = dayjs().startOf('day');
        certificado.dataEnvio = today;
      }

      this.updateForm(certificado);

      this.loadRelationshipsOptions();
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
    const certificado = this.createFromForm();
    if (certificado.id !== undefined) {
      this.subscribeToSaveResponse(this.certificadoService.update(certificado));
    } else {
      this.subscribeToSaveResponse(this.certificadoService.create(certificado));
    }
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  trackTurmaACCById(_index: number, item: ITurmaACC): number {
    return item.id!;
  }

  trackTipoAtividadeById(_index: number, item: ITipoAtividade): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICertificado>>): void {
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

  protected updateForm(certificado: ICertificado): void {
    this.editForm.patchValue({
      id: certificado.id,
      titulo: certificado.titulo,
      descricao: certificado.descricao,
      dataEnvio: certificado.dataEnvio ? certificado.dataEnvio.format(DATE_TIME_FORMAT) : null,
      observacao: certificado.observacao,
      modalidade: certificado.modalidade,
      chCuprida: certificado.chCuprida,
      pontuacao: certificado.pontuacao,
      status: certificado.status,
      caminhoArquivo: certificado.caminhoArquivo,
      usuario: certificado.usuario,
      turmaAcc: certificado.turmaAcc,
      tipoAtividade: certificado.tipoAtividade,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, certificado.usuario);
    this.turmaACCSSharedCollection = this.turmaACCService.addTurmaACCToCollectionIfMissing(
      this.turmaACCSSharedCollection,
      certificado.turmaAcc
    );
    this.tipoAtividadesSharedCollection = this.tipoAtividadeService.addTipoAtividadeToCollectionIfMissing(
      this.tipoAtividadesSharedCollection,
      certificado.tipoAtividade
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));

    this.turmaACCService
      .query()
      .pipe(map((res: HttpResponse<ITurmaACC[]>) => res.body ?? []))
      .pipe(
        map((turmaACCS: ITurmaACC[]) =>
          this.turmaACCService.addTurmaACCToCollectionIfMissing(turmaACCS, this.editForm.get('turmaAcc')!.value)
        )
      )
      .subscribe((turmaACCS: ITurmaACC[]) => (this.turmaACCSSharedCollection = turmaACCS));

    this.tipoAtividadeService
      .query()
      .pipe(map((res: HttpResponse<ITipoAtividade[]>) => res.body ?? []))
      .pipe(
        map((tipoAtividades: ITipoAtividade[]) =>
          this.tipoAtividadeService.addTipoAtividadeToCollectionIfMissing(tipoAtividades, this.editForm.get('tipoAtividade')!.value)
        )
      )
      .subscribe((tipoAtividades: ITipoAtividade[]) => (this.tipoAtividadesSharedCollection = tipoAtividades));
  }

  protected createFromForm(): ICertificado {
    return {
      ...new Certificado(),
      id: this.editForm.get(['id'])!.value,
      titulo: this.editForm.get(['titulo'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      dataEnvio: this.editForm.get(['dataEnvio'])!.value ? dayjs(this.editForm.get(['dataEnvio'])!.value, DATE_TIME_FORMAT) : undefined,
      observacao: this.editForm.get(['observacao'])!.value,
      modalidade: this.editForm.get(['modalidade'])!.value,
      chCuprida: this.editForm.get(['chCuprida'])!.value,
      pontuacao: this.editForm.get(['pontuacao'])!.value,
      status: this.editForm.get(['status'])!.value,
      caminhoArquivo: this.editForm.get(['caminhoArquivo'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      turmaAcc: this.editForm.get(['turmaAcc'])!.value,
      tipoAtividade: this.editForm.get(['tipoAtividade'])!.value,
    };
  }
}
