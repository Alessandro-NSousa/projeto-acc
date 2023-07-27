import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TurmaACCComponent } from '../list/turma-acc.component';
import { TurmaACCDetailComponent } from '../detail/turma-acc-detail.component';
import { TurmaACCUpdateComponent } from '../update/turma-acc-update.component';
import { TurmaACCRoutingResolveService } from './turma-acc-routing-resolve.service';

const turmaACCRoute: Routes = [
  {
    path: '',
    component: TurmaACCComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TurmaACCDetailComponent,
    resolve: {
      turmaACC: TurmaACCRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TurmaACCUpdateComponent,
    resolve: {
      turmaACC: TurmaACCRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TurmaACCUpdateComponent,
    resolve: {
      turmaACC: TurmaACCRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(turmaACCRoute)],
  exports: [RouterModule],
})
export class TurmaACCRoutingModule {}
