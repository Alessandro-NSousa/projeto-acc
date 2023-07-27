import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoAtividadeComponent } from './list/tipo-atividade.component';
import { TipoAtividadeDetailComponent } from './detail/tipo-atividade-detail.component';
import { TipoAtividadeUpdateComponent } from './update/tipo-atividade-update.component';
import { TipoAtividadeDeleteDialogComponent } from './delete/tipo-atividade-delete-dialog.component';
import { TipoAtividadeRoutingModule } from './route/tipo-atividade-routing.module';

@NgModule({
  imports: [SharedModule, TipoAtividadeRoutingModule],
  declarations: [TipoAtividadeComponent, TipoAtividadeDetailComponent, TipoAtividadeUpdateComponent, TipoAtividadeDeleteDialogComponent],
  entryComponents: [TipoAtividadeDeleteDialogComponent],
})
export class TipoAtividadeModule {}
