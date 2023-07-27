import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Modalidade } from 'app/entities/enumerations/modalidade.model';
import { StatusCertificado } from 'app/entities/enumerations/status-certificado.model';
import { ICertificado, Certificado } from '../certificado.model';

import { CertificadoService } from './certificado.service';

describe('Certificado Service', () => {
  let service: CertificadoService;
  let httpMock: HttpTestingController;
  let elemDefault: ICertificado;
  let expectedResult: ICertificado | ICertificado[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CertificadoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      titulo: 'AAAAAAA',
      descricao: 'AAAAAAA',
      dataEnvio: currentDate,
      observacao: 'AAAAAAA',
      modalidade: Modalidade.LOCAL,
      chCuprida: 0,
      pontuacao: 0,
      status: StatusCertificado.EM_ESPERA,
      caminhoArquivo: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dataEnvio: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Certificado', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dataEnvio: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataEnvio: currentDate,
        },
        returnedFromService
      );

      service.create(new Certificado()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Certificado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titulo: 'BBBBBB',
          descricao: 'BBBBBB',
          dataEnvio: currentDate.format(DATE_TIME_FORMAT),
          observacao: 'BBBBBB',
          modalidade: 'BBBBBB',
          chCuprida: 1,
          pontuacao: 1,
          status: 'BBBBBB',
          caminhoArquivo: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataEnvio: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Certificado', () => {
      const patchObject = Object.assign(
        {
          titulo: 'BBBBBB',
          descricao: 'BBBBBB',
          dataEnvio: currentDate.format(DATE_TIME_FORMAT),
          observacao: 'BBBBBB',
          modalidade: 'BBBBBB',
          chCuprida: 1,
        },
        new Certificado()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dataEnvio: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Certificado', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titulo: 'BBBBBB',
          descricao: 'BBBBBB',
          dataEnvio: currentDate.format(DATE_TIME_FORMAT),
          observacao: 'BBBBBB',
          modalidade: 'BBBBBB',
          chCuprida: 1,
          pontuacao: 1,
          status: 'BBBBBB',
          caminhoArquivo: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dataEnvio: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Certificado', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCertificadoToCollectionIfMissing', () => {
      it('should add a Certificado to an empty array', () => {
        const certificado: ICertificado = { id: 123 };
        expectedResult = service.addCertificadoToCollectionIfMissing([], certificado);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(certificado);
      });

      it('should not add a Certificado to an array that contains it', () => {
        const certificado: ICertificado = { id: 123 };
        const certificadoCollection: ICertificado[] = [
          {
            ...certificado,
          },
          { id: 456 },
        ];
        expectedResult = service.addCertificadoToCollectionIfMissing(certificadoCollection, certificado);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Certificado to an array that doesn't contain it", () => {
        const certificado: ICertificado = { id: 123 };
        const certificadoCollection: ICertificado[] = [{ id: 456 }];
        expectedResult = service.addCertificadoToCollectionIfMissing(certificadoCollection, certificado);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(certificado);
      });

      it('should add only unique Certificado to an array', () => {
        const certificadoArray: ICertificado[] = [{ id: 123 }, { id: 456 }, { id: 89712 }];
        const certificadoCollection: ICertificado[] = [{ id: 123 }];
        expectedResult = service.addCertificadoToCollectionIfMissing(certificadoCollection, ...certificadoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const certificado: ICertificado = { id: 123 };
        const certificado2: ICertificado = { id: 456 };
        expectedResult = service.addCertificadoToCollectionIfMissing([], certificado, certificado2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(certificado);
        expect(expectedResult).toContain(certificado2);
      });

      it('should accept null and undefined values', () => {
        const certificado: ICertificado = { id: 123 };
        expectedResult = service.addCertificadoToCollectionIfMissing([], null, certificado, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(certificado);
      });

      it('should return initial array if no Certificado is added', () => {
        const certificadoCollection: ICertificado[] = [{ id: 123 }];
        expectedResult = service.addCertificadoToCollectionIfMissing(certificadoCollection, undefined, null);
        expect(expectedResult).toEqual(certificadoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
