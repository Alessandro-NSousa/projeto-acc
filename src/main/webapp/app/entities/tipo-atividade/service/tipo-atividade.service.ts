import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITipoAtividade, getTipoAtividadeIdentifier } from '../tipo-atividade.model';

export type EntityResponseType = HttpResponse<ITipoAtividade>;
export type EntityArrayResponseType = HttpResponse<ITipoAtividade[]>;

@Injectable({ providedIn: 'root' })
export class TipoAtividadeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-atividades');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tipoAtividade: ITipoAtividade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoAtividade);
    return this.http
      .post<ITipoAtividade>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tipoAtividade: ITipoAtividade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoAtividade);
    return this.http
      .put<ITipoAtividade>(`${this.resourceUrl}/${getTipoAtividadeIdentifier(tipoAtividade) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tipoAtividade: ITipoAtividade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoAtividade);
    return this.http
      .patch<ITipoAtividade>(`${this.resourceUrl}/${getTipoAtividadeIdentifier(tipoAtividade) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITipoAtividade>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITipoAtividade[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTipoAtividadeToCollectionIfMissing(
    tipoAtividadeCollection: ITipoAtividade[],
    ...tipoAtividadesToCheck: (ITipoAtividade | null | undefined)[]
  ): ITipoAtividade[] {
    const tipoAtividades: ITipoAtividade[] = tipoAtividadesToCheck.filter(isPresent);
    if (tipoAtividades.length > 0) {
      const tipoAtividadeCollectionIdentifiers = tipoAtividadeCollection.map(
        tipoAtividadeItem => getTipoAtividadeIdentifier(tipoAtividadeItem)!
      );
      const tipoAtividadesToAdd = tipoAtividades.filter(tipoAtividadeItem => {
        const tipoAtividadeIdentifier = getTipoAtividadeIdentifier(tipoAtividadeItem);
        if (tipoAtividadeIdentifier == null || tipoAtividadeCollectionIdentifiers.includes(tipoAtividadeIdentifier)) {
          return false;
        }
        tipoAtividadeCollectionIdentifiers.push(tipoAtividadeIdentifier);
        return true;
      });
      return [...tipoAtividadesToAdd, ...tipoAtividadeCollection];
    }
    return tipoAtividadeCollection;
  }

  protected convertDateFromClient(tipoAtividade: ITipoAtividade): ITipoAtividade {
    return Object.assign({}, tipoAtividade, {
      dataCriacao: tipoAtividade.dataCriacao?.isValid() ? tipoAtividade.dataCriacao.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataCriacao = res.body.dataCriacao ? dayjs(res.body.dataCriacao) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tipoAtividade: ITipoAtividade) => {
        tipoAtividade.dataCriacao = tipoAtividade.dataCriacao ? dayjs(tipoAtividade.dataCriacao) : undefined;
      });
    }
    return res;
  }
}
