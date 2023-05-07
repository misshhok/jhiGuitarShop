import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GuitarTypeFormService } from './guitar-type-form.service';
import { GuitarTypeService } from '../service/guitar-type.service';
import { IGuitarType } from '../guitar-type.model';

import { GuitarTypeUpdateComponent } from './guitar-type-update.component';

describe('GuitarType Management Update Component', () => {
  let comp: GuitarTypeUpdateComponent;
  let fixture: ComponentFixture<GuitarTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let guitarTypeFormService: GuitarTypeFormService;
  let guitarTypeService: GuitarTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GuitarTypeUpdateComponent],
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
      .overrideTemplate(GuitarTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GuitarTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    guitarTypeFormService = TestBed.inject(GuitarTypeFormService);
    guitarTypeService = TestBed.inject(GuitarTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const guitarType: IGuitarType = { id: 456 };

      activatedRoute.data = of({ guitarType });
      comp.ngOnInit();

      expect(comp.guitarType).toEqual(guitarType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGuitarType>>();
      const guitarType = { id: 123 };
      jest.spyOn(guitarTypeFormService, 'getGuitarType').mockReturnValue(guitarType);
      jest.spyOn(guitarTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ guitarType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: guitarType }));
      saveSubject.complete();

      // THEN
      expect(guitarTypeFormService.getGuitarType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(guitarTypeService.update).toHaveBeenCalledWith(expect.objectContaining(guitarType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGuitarType>>();
      const guitarType = { id: 123 };
      jest.spyOn(guitarTypeFormService, 'getGuitarType').mockReturnValue({ id: null });
      jest.spyOn(guitarTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ guitarType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: guitarType }));
      saveSubject.complete();

      // THEN
      expect(guitarTypeFormService.getGuitarType).toHaveBeenCalled();
      expect(guitarTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGuitarType>>();
      const guitarType = { id: 123 };
      jest.spyOn(guitarTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ guitarType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(guitarTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
