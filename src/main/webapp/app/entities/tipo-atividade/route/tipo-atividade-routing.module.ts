import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoAtividadeComponent } from '../list/tipo-atividade.component';
import { TipoAtividadeDetailComponent } from '../detail/tipo-atividade-detail.component';
import { TipoAtividadeUpdateComponent } from '../update/tipo-atividade-update.component';
import { TipoAtividadeRoutingResolveService } from './tipo-atividade-routing-resolve.service';

const tipoAtividadeRoute: Routes = [
  {
    path: '',
    component: TipoAtividadeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoAtividadeDetailComponent,
    resolve: {
      tipoAtividade: TipoAtividadeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoAtividadeUpdateComponent,
    resolve: {
      tipoAtividade: TipoAtividadeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoAtividadeUpdateComponent,
    resolve: {
      tipoAtividade: TipoAtividadeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoAtividadeRoute)],
  exports: [RouterModule],
})
export class TipoAtividadeRoutingModule {}
