import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TipoAtividadeService } from '../service/tipo-atividade.service';

import { TipoAtividadeComponent } from './tipo-atividade.component';

describe('TipoAtividade Management Component', () => {
  let comp: TipoAtividadeComponent;
  let fixture: ComponentFixture<TipoAtividadeComponent>;
  let service: TipoAtividadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TipoAtividadeComponent],
    })
      .overrideTemplate(TipoAtividadeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoAtividadeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TipoAtividadeService);

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
    expect(comp.tipoAtividades?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
