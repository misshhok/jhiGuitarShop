import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GuitarDetailComponent } from './guitar-detail.component';

describe('Guitar Management Detail Component', () => {
  let comp: GuitarDetailComponent;
  let fixture: ComponentFixture<GuitarDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GuitarDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ guitar: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GuitarDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GuitarDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load guitar on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.guitar).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
