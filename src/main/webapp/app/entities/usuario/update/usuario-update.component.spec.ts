import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UsuarioService } from '../service/usuario.service';
import { IUsuario, Usuario } from '../usuario.model';
import { ITurmaACC } from 'app/entities/turma-acc/turma-acc.model';
import { TurmaACCService } from 'app/entities/turma-acc/service/turma-acc.service';
import { ICurso } from 'app/entities/curso/curso.model';
import { CursoService } from 'app/entities/curso/service/curso.service';

import { UsuarioUpdateComponent } from './usuario-update.component';

describe('Usuario Management Update Component', () => {
  let comp: UsuarioUpdateComponent;
  let fixture: ComponentFixture<UsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usuarioService: UsuarioService;
  let turmaACCService: TurmaACCService;
  let cursoService: CursoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UsuarioUpdateComponent],
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
      .overrideTemplate(UsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usuarioService = TestBed.inject(UsuarioService);
    turmaACCService = TestBed.inject(TurmaACCService);
    cursoService = TestBed.inject(CursoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TurmaACC query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const turmas: ITurmaACC[] = [{ id: 34422 }];
      usuario.turmas = turmas;

      const turmaACCCollection: ITurmaACC[] = [{ id: 17156 }];
      jest.spyOn(turmaACCService, 'query').mockReturnValue(of(new HttpResponse({ body: turmaACCCollection })));
      const additionalTurmaACCS = [...turmas];
      const expectedCollection: ITurmaACC[] = [...additionalTurmaACCS, ...turmaACCCollection];
      jest.spyOn(turmaACCService, 'addTurmaACCToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(turmaACCService.query).toHaveBeenCalled();
      expect(turmaACCService.addTurmaACCToCollectionIfMissing).toHaveBeenCalledWith(turmaACCCollection, ...additionalTurmaACCS);
      expect(comp.turmaACCSSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Curso query and add missing value', () => {
      const usuario: IUsuario = { id: 456 };
      const cursos: ICurso[] = [{ id: 85602 }];
      usuario.cursos = cursos;

      const cursoCollection: ICurso[] = [{ id: 15522 }];
      jest.spyOn(cursoService, 'query').mockReturnValue(of(new HttpResponse({ body: cursoCollection })));
      const additionalCursos = [...cursos];
      const expectedCollection: ICurso[] = [...additionalCursos, ...cursoCollection];
      jest.spyOn(cursoService, 'addCursoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(cursoService.query).toHaveBeenCalled();
      expect(cursoService.addCursoToCollectionIfMissing).toHaveBeenCalledWith(cursoCollection, ...additionalCursos);
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const usuario: IUsuario = { id: 456 };
      const turmas: ITurmaACC = { id: 47677 };
      usuario.turmas = [turmas];
      const cursos: ICurso = { id: 12244 };
      usuario.cursos = [cursos];

      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(usuario));
      expect(comp.turmaACCSSharedCollection).toContain(turmas);
      expect(comp.cursosSharedCollection).toContain(cursos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Usuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(usuarioService.update).toHaveBeenCalledWith(usuario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Usuario>>();
      const usuario = new Usuario();
      jest.spyOn(usuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuario }));
      saveSubject.complete();

      // THEN
      expect(usuarioService.create).toHaveBeenCalledWith(usuario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Usuario>>();
      const usuario = { id: 123 };
      jest.spyOn(usuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usuarioService.update).toHaveBeenCalledWith(usuario);
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

    describe('trackCursoById', () => {
      it('Should return tracked Curso primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCursoById(0, entity);
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

    describe('getSelectedCurso', () => {
      it('Should return option if no Curso is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCurso(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Curso for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCurso(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Curso is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCurso(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
