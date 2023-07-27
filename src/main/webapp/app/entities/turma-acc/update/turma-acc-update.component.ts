import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITurmaACC, TurmaACC } from '../turma-acc.model';
import { TurmaACCService } from '../service/turma-acc.service';

@Component({
  selector: 'jhi-turma-acc-update',
  templateUrl: './turma-acc-update.component.html',
})
export class TurmaACCUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    inicio: [],
    termino: [],
  });

  constructor(protected turmaACCService: TurmaACCService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ turmaACC }) => {
      this.updateForm(turmaACC);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const turmaACC = this.createFromForm();
    if (turmaACC.id !== undefined) {
      this.subscribeToSaveResponse(this.turmaACCService.update(turmaACC));
    } else {
      this.subscribeToSaveResponse(this.turmaACCService.create(turmaACC));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITurmaACC>>): void {
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

  protected updateForm(turmaACC: ITurmaACC): void {
    this.editForm.patchValue({
      id: turmaACC.id,
      nome: turmaACC.nome,
      inicio: turmaACC.inicio,
      termino: turmaACC.termino,
    });
  }

  protected createFromForm(): ITurmaACC {
    return {
      ...new TurmaACC(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      inicio: this.editForm.get(['inicio'])!.value,
      termino: this.editForm.get(['termino'])!.value,
    };
  }
}
