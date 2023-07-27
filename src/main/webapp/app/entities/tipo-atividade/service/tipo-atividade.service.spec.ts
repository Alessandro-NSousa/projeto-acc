import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITipoAtividade, TipoAtividade } from '../tipo-atividade.model';

import { TipoAtividadeService } from './tipo-atividade.service';

describe('TipoAtividade Service', () => {
  let service: TipoAtividadeService;
  let httpMock: HttpTestingController;
  let elemDefault: ITipoAtividade;
  let expectedResult: ITipoAtividade | ITipoAtividade[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoAtividadeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nome: 'AAAAAAA',
      descricao: 'AAAAAAA',
      numeroPontos: 0,
      dataCriacao: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dataCriacao: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TipoAtividade', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dataCriacao: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataCriacao: currentDate,
        },
        returnedFromService
      );

      service.create(new TipoAtividade()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoAtividade', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          descricao: 'BBBBBB',
          numeroPontos: 1,
          dataCriacao: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataCriacao: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoAtividade', () => {
      const patchObject = Object.assign(
        {
          nome: 'BBBBBB',
          numeroPontos: 1,
        },
        new TipoAtividade()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dataCriacao: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoAtividade', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          descricao: 'BBBBBB',
          numeroPontos: 1,
          dataCriacao: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataCriacao: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TipoAtividade', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTipoAtividadeToCollectionIfMissing', () => {
      it('should add a TipoAtividade to an empty array', () => {
        const tipoAtividade: ITipoAtividade = { id: 123 };
        expectedResult = service.addTipoAtividadeToCollectionIfMissing([], tipoAtividade);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoAtividade);
      });

      it('should not add a TipoAtividade to an array that contains it', () => {
        const tipoAtividade: ITipoAtividade = { id: 123 };
        const tipoAtividadeCollection: ITipoAtividade[] = [
          {
            ...tipoAtividade,
          },
          { id: 456 },
        ];
        expectedResult = service.addTipoAtividadeToCollectionIfMissing(tipoAtividadeCollection, tipoAtividade);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoAtividade to an array that doesn't contain it", () => {
        const tipoAtividade: ITipoAtividade = { id: 123 };
        const tipoAtividadeCollection: ITipoAtividade[] = [{ id: 456 }];
        expectedResult = service.addTipoAtividadeToCollectionIfMissing(tipoAtividadeCollection, tipoAtividade);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoAtividade);
      });

      it('should add only unique TipoAtividade to an array', () => {
        const tipoAtividadeArray: ITipoAtividade[] = [{ id: 123 }, { id: 456 }, { id: 5305 }];
        const tipoAtividadeCollection: ITipoAtividade[] = [{ id: 123 }];
        expectedResult = service.addTipoAtividadeToCollectionIfMissing(tipoAtividadeCollection, ...tipoAtividadeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoAtividade: ITipoAtividade = { id: 123 };
        const tipoAtividade2: ITipoAtividade = { id: 456 };
        expectedResult = service.addTipoAtividadeToCollectionIfMissing([], tipoAtividade, tipoAtividade2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoAtividade);
        expect(expectedResult).toContain(tipoAtividade2);
      });

      it('should accept null and undefined values', () => {
        const tipoAtividade: ITipoAtividade = { id: 123 };
        expectedResult = service.addTipoAtividadeToCollectionIfMissing([], null, tipoAtividade, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoAtividade);
      });

      it('should return initial array if no TipoAtividade is added', () => {
        const tipoAtividadeCollection: ITipoAtividade[] = [{ id: 123 }];
        expectedResult = service.addTipoAtividadeToCollectionIfMissing(tipoAtividadeCollection, undefined, null);
        expect(expectedResult).toEqual(tipoAtividadeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
