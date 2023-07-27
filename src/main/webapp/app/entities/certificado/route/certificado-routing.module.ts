import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CertificadoComponent } from '../list/certificado.component';
import { CertificadoDetailComponent } from '../detail/certificado-detail.component';
import { CertificadoUpdateComponent } from '../update/certificado-update.component';
import { CertificadoRoutingResolveService } from './certificado-routing-resolve.service';

const certificadoRoute: Routes = [
  {
    path: '',
    component: CertificadoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CertificadoDetailComponent,
    resolve: {
      certificado: CertificadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CertificadoUpdateComponent,
    resolve: {
      certificado: CertificadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CertificadoUpdateComponent,
    resolve: {
      certificado: CertificadoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(certificadoRoute)],
  exports: [RouterModule],
})
export class CertificadoRoutingModule {}
