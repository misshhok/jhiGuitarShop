import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GuitarFormService } from './guitar-form.service';
import { GuitarService } from '../service/guitar.service';
import { IGuitar } from '../guitar.model';
import { IGuitarType } from 'app/entities/guitar-type/guitar-type.model';
import { GuitarTypeService } from 'app/entities/guitar-type/service/guitar-type.service';

import { GuitarUpdateComponent } from './guitar-update.component';

describe('Guitar Management Update Component', () => {
  let comp: GuitarUpdateComponent;
  let fixture: ComponentFixture<GuitarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let guitarFormService: GuitarFormService;
  let guitarService: GuitarService;
  let guitarTypeService: GuitarTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GuitarUpdateComponent],
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
      .overrideTemplate(GuitarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GuitarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    guitarFormService = TestBed.inject(GuitarFormService);
    guitarService = TestBed.inject(GuitarService);
    guitarTypeService = TestBed.inject(GuitarTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call GuitarType query and add missing value', () => {
      const guitar: IGuitar = { id: 456 };
      const guitarType: IGuitarType = { id: 38051 };
      guitar.guitarType = guitarType;

      const guitarTypeCollection: IGuitarType[] = [{ id: 90482 }];
      jest.spyOn(guitarTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: guitarTypeCollection })));
      const additionalGuitarTypes = [guitarType];
      const expectedCollection: IGuitarType[] = [...additionalGuitarTypes, ...guitarTypeCollection];
      jest.spyOn(guitarTypeService, 'addGuitarTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ guitar });
      comp.ngOnInit();

      expect(guitarTypeService.query).toHaveBeenCalled();
      expect(guitarTypeService.addGuitarTypeToCollectionIfMissing).toHaveBeenCalledWith(
        guitarTypeCollection,
        ...additionalGuitarTypes.map(expect.objectContaining)
      );
      expect(comp.guitarTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const guitar: IGuitar = { id: 456 };
      const guitarType: IGuitarType = { id: 5276 };
      guitar.guitarType = guitarType;

      activatedRoute.data = of({ guitar });
      comp.ngOnInit();

      expect(comp.guitarTypesSharedCollection).toContain(guitarType);
      expect(comp.guitar).toEqual(guitar);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGuitar>>();
      const guitar = { id: 123 };
      jest.spyOn(guitarFormService, 'getGuitar').mockReturnValue(guitar);
      jest.spyOn(guitarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ guitar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: guitar }));
      saveSubject.complete();

      // THEN
      expect(guitarFormService.getGuitar).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(guitarService.update).toHaveBeenCalledWith(expect.objectContaining(guitar));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGuitar>>();
      const guitar = { id: 123 };
      jest.spyOn(guitarFormService, 'getGuitar').mockReturnValue({ id: null });
      jest.spyOn(guitarService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ guitar: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: guitar }));
      saveSubject.complete();

      // THEN
      expect(guitarFormService.getGuitar).toHaveBeenCalled();
      expect(guitarService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGuitar>>();
      const guitar = { id: 123 };
      jest.spyOn(guitarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ guitar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(guitarService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGuitarType', () => {
      it('Should forward to guitarTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(guitarTypeService, 'compareGuitarType');
        comp.compareGuitarType(entity, entity2);
        expect(guitarTypeService.compareGuitarType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
