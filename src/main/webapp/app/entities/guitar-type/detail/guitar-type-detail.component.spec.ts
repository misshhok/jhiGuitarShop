import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GuitarTypeDetailComponent } from './guitar-type-detail.component';

describe('GuitarType Management Detail Component', () => {
  let comp: GuitarTypeDetailComponent;
  let fixture: ComponentFixture<GuitarTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GuitarTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ guitarType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GuitarTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GuitarTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load guitarType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.guitarType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
