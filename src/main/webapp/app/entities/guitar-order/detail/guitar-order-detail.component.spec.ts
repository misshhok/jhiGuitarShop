import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GuitarOrderDetailComponent } from './guitar-order-detail.component';

describe('GuitarOrder Management Detail Component', () => {
  let comp: GuitarOrderDetailComponent;
  let fixture: ComponentFixture<GuitarOrderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GuitarOrderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ guitarOrder: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GuitarOrderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GuitarOrderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load guitarOrder on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.guitarOrder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
