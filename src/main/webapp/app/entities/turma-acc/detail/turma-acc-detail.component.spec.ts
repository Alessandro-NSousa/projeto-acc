import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TurmaACCDetailComponent } from './turma-acc-detail.component';

describe('TurmaACC Management Detail Component', () => {
  let comp: TurmaACCDetailComponent;
  let fixture: ComponentFixture<TurmaACCDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TurmaACCDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ turmaACC: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TurmaACCDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TurmaACCDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load turmaACC on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.turmaACC).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
