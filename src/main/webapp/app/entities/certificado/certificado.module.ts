import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CertificadoComponent } from './list/certificado.component';
import { CertificadoDetailComponent } from './detail/certificado-detail.component';
import { CertificadoUpdateComponent } from './update/certificado-update.component';
import { CertificadoDeleteDialogComponent } from './delete/certificado-delete-dialog.component';
import { CertificadoRoutingModule } from './route/certificado-routing.module';

@NgModule({
  imports: [SharedModule, CertificadoRoutingModule],
  declarations: [CertificadoComponent, CertificadoDetailComponent, CertificadoUpdateComponent, CertificadoDeleteDialogComponent],
  entryComponents: [CertificadoDeleteDialogComponent],
})
export class CertificadoModule {}
