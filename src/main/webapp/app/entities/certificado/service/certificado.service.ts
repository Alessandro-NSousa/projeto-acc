import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICertificado, getCertificadoIdentifier } from '../certificado.model';

export type EntityResponseType = HttpResponse<ICertificado>;
export type EntityArrayResponseType = HttpResponse<ICertificado[]>;

@Injectable({ providedIn: 'root' })
export class CertificadoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/certificados');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(certificado: ICertificado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(certificado);
    return this.http
      .post<ICertificado>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(certificado: ICertificado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(certificado);
    return this.http
      .put<ICertificado>(`${this.resourceUrl}/${getCertificadoIdentifier(certificado) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(certificado: ICertificado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(certificado);
    return this.http
      .patch<ICertificado>(`${this.resourceUrl}/${getCertificadoIdentifier(certificado) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICertificado>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICertificado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCertificadoToCollectionIfMissing(
    certificadoCollection: ICertificado[],
    ...certificadosToCheck: (ICertificado | null | undefined)[]
  ): ICertificado[] {
    const certificados: ICertificado[] = certificadosToCheck.filter(isPresent);
    if (certificados.length > 0) {
      const certificadoCollectionIdentifiers = certificadoCollection.map(certificadoItem => getCertificadoIdentifier(certificadoItem)!);
      const certificadosToAdd = certificados.filter(certificadoItem => {
        const certificadoIdentifier = getCertificadoIdentifier(certificadoItem);
        if (certificadoIdentifier == null || certificadoCollectionIdentifiers.includes(certificadoIdentifier)) {
          return false;
        }
        certificadoCollectionIdentifiers.push(certificadoIdentifier);
        return true;
      });
      return [...certificadosToAdd, ...certificadoCollection];
    }
    return certificadoCollection;
  }

  protected convertDateFromClient(certificado: ICertificado): ICertificado {
    return Object.assign({}, certificado, {
      dataEnvio: certificado.dataEnvio?.isValid() ? certificado.dataEnvio.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataEnvio = res.body.dataEnvio ? dayjs(res.body.dataEnvio) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((certificado: ICertificado) => {
        certificado.dataEnvio = certificado.dataEnvio ? dayjs(certificado.dataEnvio) : undefined;
      });
    }
    return res;
  }
}
