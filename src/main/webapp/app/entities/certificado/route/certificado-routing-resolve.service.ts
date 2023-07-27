import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICertificado, Certificado } from '../certificado.model';
import { CertificadoService } from '../service/certificado.service';

@Injectable({ providedIn: 'root' })
export class CertificadoRoutingResolveService implements Resolve<ICertificado> {
  constructor(protected service: CertificadoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICertificado> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((certificado: HttpResponse<Certificado>) => {
          if (certificado.body) {
            return of(certificado.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Certificado());
  }
}
