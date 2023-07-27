import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICurso, getCursoIdentifier } from '../curso.model';

export type EntityResponseType = HttpResponse<ICurso>;
export type EntityArrayResponseType = HttpResponse<ICurso[]>;

@Injectable({ providedIn: 'root' })
export class CursoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cursos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(curso: ICurso): Observable<EntityResponseType> {
    return this.http.post<ICurso>(this.resourceUrl, curso, { observe: 'response' });
  }

  update(curso: ICurso): Observable<EntityResponseType> {
    return this.http.put<ICurso>(`${this.resourceUrl}/${getCursoIdentifier(curso) as number}`, curso, { observe: 'response' });
  }

  partialUpdate(curso: ICurso): Observable<EntityResponseType> {
    return this.http.patch<ICurso>(`${this.resourceUrl}/${getCursoIdentifier(curso) as number}`, curso, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICurso>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICurso[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCursoToCollectionIfMissing(cursoCollection: ICurso[], ...cursosToCheck: (ICurso | null | undefined)[]): ICurso[] {
    const cursos: ICurso[] = cursosToCheck.filter(isPresent);
    if (cursos.length > 0) {
      const cursoCollectionIdentifiers = cursoCollection.map(cursoItem => getCursoIdentifier(cursoItem)!);
      const cursosToAdd = cursos.filter(cursoItem => {
        const cursoIdentifier = getCursoIdentifier(cursoItem);
        if (cursoIdentifier == null || cursoCollectionIdentifiers.includes(cursoIdentifier)) {
          return false;
        }
        cursoCollectionIdentifiers.push(cursoIdentifier);
        return true;
      });
      return [...cursosToAdd, ...cursoCollection];
    }
    return cursoCollection;
  }
}
