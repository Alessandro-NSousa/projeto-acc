import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoAtividade, TipoAtividade } from '../tipo-atividade.model';
import { TipoAtividadeService } from '../service/tipo-atividade.service';

@Injectable({ providedIn: 'root' })
export class TipoAtividadeRoutingResolveService implements Resolve<ITipoAtividade> {
  constructor(protected service: TipoAtividadeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoAtividade> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoAtividade: HttpResponse<TipoAtividade>) => {
          if (tipoAtividade.body) {
            return of(tipoAtividade.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoAtividade());
  }
}
