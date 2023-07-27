import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITurmaACC, getTurmaACCIdentifier } from '../turma-acc.model';

export type EntityResponseType = HttpResponse<ITurmaACC>;
export type EntityArrayResponseType = HttpResponse<ITurmaACC[]>;

@Injectable({ providedIn: 'root' })
export class TurmaACCService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/turma-accs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(turmaACC: ITurmaACC): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(turmaACC);
    return this.http
      .post<ITurmaACC>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(turmaACC: ITurmaACC): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(turmaACC);
    return this.http
      .put<ITurmaACC>(`${this.resourceUrl}/${getTurmaACCIdentifier(turmaACC) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(turmaACC: ITurmaACC): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(turmaACC);
    return this.http
      .patch<ITurmaACC>(`${this.resourceUrl}/${getTurmaACCIdentifier(turmaACC) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITurmaACC>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITurmaACC[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTurmaACCToCollectionIfMissing(turmaACCCollection: ITurmaACC[], ...turmaACCSToCheck: (ITurmaACC | null | undefined)[]): ITurmaACC[] {
    const turmaACCS: ITurmaACC[] = turmaACCSToCheck.filter(isPresent);
    if (turmaACCS.length > 0) {
      const turmaACCCollectionIdentifiers = turmaACCCollection.map(turmaACCItem => getTurmaACCIdentifier(turmaACCItem)!);
      const turmaACCSToAdd = turmaACCS.filter(turmaACCItem => {
        const turmaACCIdentifier = getTurmaACCIdentifier(turmaACCItem);
        if (turmaACCIdentifier == null || turmaACCCollectionIdentifiers.includes(turmaACCIdentifier)) {
          return false;
        }
        turmaACCCollectionIdentifiers.push(turmaACCIdentifier);
        return true;
      });
      return [...turmaACCSToAdd, ...turmaACCCollection];
    }
    return turmaACCCollection;
  }

  protected convertDateFromClient(turmaACC: ITurmaACC): ITurmaACC {
    return Object.assign({}, turmaACC, {
      inicio: turmaACC.inicio?.isValid() ? turmaACC.inicio.format(DATE_FORMAT) : undefined,
      termino: turmaACC.termino?.isValid() ? turmaACC.termino.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.inicio = res.body.inicio ? dayjs(res.body.inicio) : undefined;
      res.body.termino = res.body.termino ? dayjs(res.body.termino) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((turmaACC: ITurmaACC) => {
        turmaACC.inicio = turmaACC.inicio ? dayjs(turmaACC.inicio) : undefined;
        turmaACC.termino = turmaACC.termino ? dayjs(turmaACC.termino) : undefined;
      });
    }
    return res;
  }
}
