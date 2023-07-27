import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsuario, Usuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { ITurmaACC } from 'app/entities/turma-acc/turma-acc.model';
import { TurmaACCService } from 'app/entities/turma-acc/service/turma-acc.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';
import { Perfil } from 'app/entities/enumerations/perfil.model';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;
  perfilValues = Object.keys(Perfil);

  turmaACCSSharedCollection: ITurmaACC[] = [];
  cursosSharedCollection: ICurso[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    login: [null, [Validators.required]],
    senha: [null, [Validators.required]],
    dataCadastro: [],
    ultimoAcesso: [],
    perfil: [null, [Validators.required]],
    turmas: [],
    cursos: [],
  });

  constructor(
    protected usuarioService: UsuarioService,
    protected turmaACCService: TurmaACCService,
    protected cursoService: CursoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      if (usuario.id === undefined) {
        const today = dayjs().startOf('day');
        usuario.dataCadastro = today;
        usuario.ultimoAcesso = today;
      }

      this.updateForm(usuario);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.createFromForm();
    if (usuario.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  trackTurmaACCById(_index: number, item: ITurmaACC): number {
    return item.id!;
  }

  trackCursoById(_index: number, item: ICurso): number {
    return item.id!;
  }

  getSelectedTurmaACC(option: ITurmaACC, selectedVals?: ITurmaACC[]): ITurmaACC {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedCurso(option: ICurso, selectedVals?: ICurso[]): ICurso {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
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

  protected updateForm(usuario: IUsuario): void {
    this.editForm.patchValue({
      id: usuario.id,
      nome: usuario.nome,
      login: usuario.login,
      senha: usuario.senha,
      dataCadastro: usuario.dataCadastro ? usuario.dataCadastro.format(DATE_TIME_FORMAT) : null,
      ultimoAcesso: usuario.ultimoAcesso ? usuario.ultimoAcesso.format(DATE_TIME_FORMAT) : null,
      perfil: usuario.perfil,
      turmas: usuario.turmas,
      cursos: usuario.cursos,
    });

    this.turmaACCSSharedCollection = this.turmaACCService.addTurmaACCToCollectionIfMissing(
      this.turmaACCSSharedCollection,
      ...(usuario.turmas ?? [])
    );
    this.cursosSharedCollection = this.cursoService.addCursoToCollectionIfMissing(this.cursosSharedCollection, ...(usuario.cursos ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.turmaACCService
      .query()
      .pipe(map((res: HttpResponse<ITurmaACC[]>) => res.body ?? []))
      .pipe(
        map((turmaACCS: ITurmaACC[]) =>
          this.turmaACCService.addTurmaACCToCollectionIfMissing(turmaACCS, ...(this.editForm.get('turmas')!.value ?? []))
        )
      )
      .subscribe((turmaACCS: ITurmaACC[]) => (this.turmaACCSSharedCollection = turmaACCS));

    this.cursoService
      .query()
      .pipe(map((res: HttpResponse<ICurso[]>) => res.body ?? []))
      .pipe(
        map((cursos: ICurso[]) => this.cursoService.addCursoToCollectionIfMissing(cursos, ...(this.editForm.get('cursos')!.value ?? [])))
      )
      .subscribe((cursos: ICurso[]) => (this.cursosSharedCollection = cursos));
  }

  protected createFromForm(): IUsuario {
    return {
      ...new Usuario(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      login: this.editForm.get(['login'])!.value,
      senha: this.editForm.get(['senha'])!.value,
      dataCadastro: this.editForm.get(['dataCadastro'])!.value
        ? dayjs(this.editForm.get(['dataCadastro'])!.value, DATE_TIME_FORMAT)
        : undefined,
      ultimoAcesso: this.editForm.get(['ultimoAcesso'])!.value
        ? dayjs(this.editForm.get(['ultimoAcesso'])!.value, DATE_TIME_FORMAT)
        : undefined,
      perfil: this.editForm.get(['perfil'])!.value,
      turmas: this.editForm.get(['turmas'])!.value,
      cursos: this.editForm.get(['cursos'])!.value,
    };
  }
}
