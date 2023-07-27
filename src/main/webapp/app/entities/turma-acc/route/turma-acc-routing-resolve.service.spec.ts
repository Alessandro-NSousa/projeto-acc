import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITurmaACC, TurmaACC } from '../turma-acc.model';
import { TurmaACCService } from '../service/turma-acc.service';

import { TurmaACCRoutingResolveService } from './turma-acc-routing-resolve.service';

describe('TurmaACC routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TurmaACCRoutingResolveService;
  let service: TurmaACCService;
  let resultTurmaACC: ITurmaACC | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TurmaACCRoutingResolveService);
    service = TestBed.inject(TurmaACCService);
    resultTurmaACC = undefined;
  });

  describe('resolve', () => {
    it('should return ITurmaACC returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTurmaACC = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTurmaACC).toEqual({ id: 123 });
    });

    it('should return new ITurmaACC if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTurmaACC = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTurmaACC).toEqual(new TurmaACC());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TurmaACC })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTurmaACC = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTurmaACC).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
