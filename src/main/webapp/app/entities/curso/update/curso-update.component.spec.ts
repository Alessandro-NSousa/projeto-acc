import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CursoService } from '../service/curso.service';
import { ICurso, Curso } from '../curso.model';
import { ITurmaACC } from 'app/entities/turma-acc/turma-acc.model';
import { TurmaACCService } from 'app/entities/turma-acc/service/turma-acc.service';

import { CursoUpdateComponent } from './curso-update.component';

describe('Curso Management Update Component', () => {
  let comp: CursoUpdateComponent;
  let fixture: ComponentFixture<CursoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cursoService: CursoService;
  let turmaACCService: TurmaACCService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CursoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CursoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CursoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cursoService = TestBed.inject(CursoService);
    turmaACCService = TestBed.inject(TurmaACCService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TurmaACC query and add missing value', () => {
      const curso: ICurso = { id: 456 };
      const turmas: ITurmaACC[] = [{ id: 94009 }];
      curso.turmas = turmas;

      const turmaACCCollection: ITurmaACC[] = [{ id: 84216 }];
      jest.spyOn(turmaACCService, 'query').mockReturnValue(of(new HttpResponse({ body: turmaACCCollection })));
      const additionalTurmaACCS = [...turmas];
      const expectedCollection: ITurmaACC[] = [...additionalTurmaACCS, ...turmaACCCollection];
      jest.spyOn(turmaACCService, 'addTurmaACCToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      expect(turmaACCService.query).toHaveBeenCalled();
      expect(turmaACCService.addTurmaACCToCollectionIfMissing).toHaveBeenCalledWith(turmaACCCollection, ...additionalTurmaACCS);
      expect(comp.turmaACCSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const curso: ICurso = { id: 456 };
      const turmas: ITurmaACC = { id: 2718 };
      curso.turmas = [turmas];

      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(curso));
      expect(comp.turmaACCSSharedCollection).toContain(turmas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Curso>>();
      const curso = { id: 123 };
      jest.spyOn(cursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curso }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cursoService.update).toHaveBeenCalledWith(curso);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Curso>>();
      const curso = new Curso();
      jest.spyOn(cursoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curso }));
      saveSubject.complete();

      // THEN
      expect(cursoService.create).toHaveBeenCalledWith(curso);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Curso>>();
      const curso = { id: 123 };
      jest.spyOn(cursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cursoService.update).toHaveBeenCalledWith(curso);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTurmaACCById', () => {
      it('Should return tracked TurmaACC primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTurmaACCById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedTurmaACC', () => {
      it('Should return option if no TurmaACC is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTurmaACC(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected TurmaACC for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTurmaACC(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this TurmaACC is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTurmaACC(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
