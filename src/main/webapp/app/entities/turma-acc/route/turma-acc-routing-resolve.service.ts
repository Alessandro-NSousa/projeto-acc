import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITurmaACC, TurmaACC } from '../turma-acc.model';
import { TurmaACCService } from '../service/turma-acc.service';

@Injectable({ providedIn: 'root' })
export class TurmaACCRoutingResolveService implements Resolve<ITurmaACC> {
  constructor(protected service: TurmaACCService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITurmaACC> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((turmaACC: HttpResponse<TurmaACC>) => {
          if (turmaACC.body) {
            return of(turmaACC.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TurmaACC());
  }
}
