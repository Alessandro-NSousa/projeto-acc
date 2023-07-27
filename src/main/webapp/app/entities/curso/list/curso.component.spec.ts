import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CursoService } from '../service/curso.service';

import { CursoComponent } from './curso.component';

describe('Curso Management Component', () => {
  let comp: CursoComponent;
  let fixture: ComponentFixture<CursoComponent>;
  let service: CursoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CursoComponent],
    })
      .overrideTemplate(CursoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CursoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CursoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.cursos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
