import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TurmaACCComponent } from './list/turma-acc.component';
import { TurmaACCDetailComponent } from './detail/turma-acc-detail.component';
import { TurmaACCUpdateComponent } from './update/turma-acc-update.component';
import { TurmaACCDeleteDialogComponent } from './delete/turma-acc-delete-dialog.component';
import { TurmaACCRoutingModule } from './route/turma-acc-routing.module';

@NgModule({
  imports: [SharedModule, TurmaACCRoutingModule],
  declarations: [TurmaACCComponent, TurmaACCDetailComponent, TurmaACCUpdateComponent, TurmaACCDeleteDialogComponent],
  entryComponents: [TurmaACCDeleteDialogComponent],
})
export class TurmaACCModule {}
