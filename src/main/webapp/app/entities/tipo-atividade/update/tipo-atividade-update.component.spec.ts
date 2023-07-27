import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoAtividadeService } from '../service/tipo-atividade.service';
import { ITipoAtividade, TipoAtividade } from '../tipo-atividade.model';

import { TipoAtividadeUpdateComponent } from './tipo-atividade-update.component';

describe('TipoAtividade Management Update Component', () => {
  let comp: TipoAtividadeUpdateComponent;
  let fixture: ComponentFixture<TipoAtividadeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoAtividadeService: TipoAtividadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TipoAtividadeUpdateComponent],
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
      .overrideTemplate(TipoAtividadeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoAtividadeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoAtividadeService = TestBed.inject(TipoAtividadeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoAtividade: ITipoAtividade = { id: 456 };

      activatedRoute.data = of({ tipoAtividade });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tipoAtividade));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoAtividade>>();
      const tipoAtividade = { id: 123 };
      jest.spyOn(tipoAtividadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoAtividade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoAtividade }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoAtividadeService.update).toHaveBeenCalledWith(tipoAtividade);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoAtividade>>();
      const tipoAtividade = new TipoAtividade();
      jest.spyOn(tipoAtividadeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoAtividade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoAtividade }));
      saveSubject.complete();

      // THEN
      expect(tipoAtividadeService.create).toHaveBeenCalledWith(tipoAtividade);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TipoAtividade>>();
      const tipoAtividade = { id: 123 };
      jest.spyOn(tipoAtividadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoAtividade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoAtividadeService.update).toHaveBeenCalledWith(tipoAtividade);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
