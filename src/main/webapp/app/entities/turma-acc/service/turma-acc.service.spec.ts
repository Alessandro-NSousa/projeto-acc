import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITurmaACC, TurmaACC } from '../turma-acc.model';

import { TurmaACCService } from './turma-acc.service';

describe('TurmaACC Service', () => {
  let service: TurmaACCService;
  let httpMock: HttpTestingController;
  let elemDefault: ITurmaACC;
  let expectedResult: ITurmaACC | ITurmaACC[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TurmaACCService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nome: 'AAAAAAA',
      inicio: currentDate,
      termino: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          inicio: currentDate.format(DATE_FORMAT),
          termino: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TurmaACC', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          inicio: currentDate.format(DATE_FORMAT),
          termino: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          inicio: currentDate,
          termino: currentDate,
        },
        returnedFromService
      );

      service.create(new TurmaACC()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TurmaACC', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          inicio: currentDate.format(DATE_FORMAT),
          termino: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          inicio: currentDate,
          termino: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TurmaACC', () => {
      const patchObject = Object.assign({}, new TurmaACC());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          inicio: currentDate,
          termino: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TurmaACC', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
          inicio: currentDate.format(DATE_FORMAT),
          termino: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          inicio: currentDate,
          termino: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TurmaACC', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTurmaACCToCollectionIfMissing', () => {
      it('should add a TurmaACC to an empty array', () => {
        const turmaACC: ITurmaACC = { id: 123 };
        expectedResult = service.addTurmaACCToCollectionIfMissing([], turmaACC);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(turmaACC);
      });

      it('should not add a TurmaACC to an array that contains it', () => {
        const turmaACC: ITurmaACC = { id: 123 };
        const turmaACCCollection: ITurmaACC[] = [
          {
            ...turmaACC,
          },
          { id: 456 },
        ];
        expectedResult = service.addTurmaACCToCollectionIfMissing(turmaACCCollection, turmaACC);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TurmaACC to an array that doesn't contain it", () => {
        const turmaACC: ITurmaACC = { id: 123 };
        const turmaACCCollection: ITurmaACC[] = [{ id: 456 }];
        expectedResult = service.addTurmaACCToCollectionIfMissing(turmaACCCollection, turmaACC);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(turmaACC);
      });

      it('should add only unique TurmaACC to an array', () => {
        const turmaACCArray: ITurmaACC[] = [{ id: 123 }, { id: 456 }, { id: 76891 }];
        const turmaACCCollection: ITurmaACC[] = [{ id: 123 }];
        expectedResult = service.addTurmaACCToCollectionIfMissing(turmaACCCollection, ...turmaACCArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const turmaACC: ITurmaACC = { id: 123 };
        const turmaACC2: ITurmaACC = { id: 456 };
        expectedResult = service.addTurmaACCToCollectionIfMissing([], turmaACC, turmaACC2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(turmaACC);
        expect(expectedResult).toContain(turmaACC2);
      });

      it('should accept null and undefined values', () => {
        const turmaACC: ITurmaACC = { id: 123 };
        expectedResult = service.addTurmaACCToCollectionIfMissing([], null, turmaACC, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(turmaACC);
      });

      it('should return initial array if no TurmaACC is added', () => {
        const turmaACCCollection: ITurmaACC[] = [{ id: 123 }];
        expectedResult = service.addTurmaACCToCollectionIfMissing(turmaACCCollection, undefined, null);
        expect(expectedResult).toEqual(turmaACCCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
