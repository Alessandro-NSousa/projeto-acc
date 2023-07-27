import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICurso, Curso } from '../curso.model';
import { CursoService } from '../service/curso.service';
import { ITurmaACC } from 'app/entities/turma-acc/turma-acc.model';
import { TurmaACCService } from 'app/entities/turma-acc/service/turma-acc.service';

@Component({
  selector: 'jhi-curso-update',
  templateUrl: './curso-update.component.html',
})
export class CursoUpdateComponent implements OnInit {
  isSaving = false;

  turmaACCSSharedCollection: ITurmaACC[] = [];

  editForm = this.fb.group({
    id: [],
    nomeCurso: [null, [Validators.required]],
    sigla: [],
    turmas: [],
  });

  constructor(
    protected cursoService: CursoService,
    protected turmaACCService: TurmaACCService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ curso }) => {
      this.updateForm(curso);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const curso = this.createFromForm();
    if (curso.id !== undefined) {
      this.subscribeToSaveResponse(this.cursoService.update(curso));
    } else {
      this.subscribeToSaveResponse(this.cursoService.create(curso));
    }
  }

  trackTurmaACCById(_index: number, item: ITurmaACC): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurso>>): void {
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

  protected updateForm(curso: ICurso): void {
    this.editForm.patchValue({
      id: curso.id,
      nomeCurso: curso.nomeCurso,
      sigla: curso.sigla,
      turmas: curso.turmas,
    });

    this.turmaACCSSharedCollection = this.turmaACCService.addTurmaACCToCollectionIfMissing(
      this.turmaACCSSharedCollection,
      ...(curso.turmas ?? [])
    );
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
  }

  protected createFromForm(): ICurso {
    return {
      ...new Curso(),
      id: this.editForm.get(['id'])!.value,
      nomeCurso: this.editForm.get(['nomeCurso'])!.value,
      sigla: this.editForm.get(['sigla'])!.value,
      turmas: this.editForm.get(['turmas'])!.value,
    };
  }
}
