import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GuitarOrderFormService } from './guitar-order-form.service';
import { GuitarOrderService } from '../service/guitar-order.service';
import { IGuitarOrder } from '../guitar-order.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IGuitar } from 'app/entities/guitar/guitar.model';
import { GuitarService } from 'app/entities/guitar/service/guitar.service';

import { GuitarOrderUpdateComponent } from './guitar-order-update.component';

describe('GuitarOrder Management Update Component', () => {
  let comp: GuitarOrderUpdateComponent;
  let fixture: ComponentFixture<GuitarOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let guitarOrderFormService: GuitarOrderFormService;
  let guitarOrderService: GuitarOrderService;
  let userService: UserService;
  let guitarService: GuitarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GuitarOrderUpdateComponent],
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
      .overrideTemplate(GuitarOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GuitarOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    guitarOrderFormService = TestBed.inject(GuitarOrderFormService);
    guitarOrderService = TestBed.inject(GuitarOrderService);
    userService = TestBed.inject(UserService);
    guitarService = TestBed.inject(GuitarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const guitarOrder: IGuitarOrder = { id: 456 };
      const orderOwner: IUser = { id: 2558 };
      guitarOrder.orderOwner = orderOwner;

      const userCollection: IUser[] = [{ id: 97380 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [orderOwner];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ guitarOrder });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Guitar query and add missing value', () => {
      const guitarOrder: IGuitarOrder = { id: 456 };
      const guitarsInOrders: IGuitar[] = [{ id: 85915 }];
      guitarOrder.guitarsInOrders = guitarsInOrders;

      const guitarCollection: IGuitar[] = [{ id: 83144 }];
      jest.spyOn(guitarService, 'query').mockReturnValue(of(new HttpResponse({ body: guitarCollection })));
      const additionalGuitars = [...guitarsInOrders];
      const expectedCollection: IGuitar[] = [...additionalGuitars, ...guitarCollection];
      jest.spyOn(guitarService, 'addGuitarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ guitarOrder });
      comp.ngOnInit();

      expect(guitarService.query).toHaveBeenCalled();
      expect(guitarService.addGuitarToCollectionIfMissing).toHaveBeenCalledWith(
        guitarCollection,
        ...additionalGuitars.map(expect.objectContaining)
      );
      expect(comp.guitarsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const guitarOrder: IGuitarOrder = { id: 456 };
      const orderOwner: IUser = { id: 66972 };
      guitarOrder.orderOwner = orderOwner;
      const guitarsInOrder: IGuitar = { id: 32322 };
      guitarOrder.guitarsInOrders = [guitarsInOrder];

      activatedRoute.data = of({ guitarOrder });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(orderOwner);
      expect(comp.guitarsSharedCollection).toContain(guitarsInOrder);
      expect(comp.guitarOrder).toEqual(guitarOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGuitarOrder>>();
      const guitarOrder = { id: 123 };
      jest.spyOn(guitarOrderFormService, 'getGuitarOrder').mockReturnValue(guitarOrder);
      jest.spyOn(guitarOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ guitarOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: guitarOrder }));
      saveSubject.complete();

      // THEN
      expect(guitarOrderFormService.getGuitarOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(guitarOrderService.update).toHaveBeenCalledWith(expect.objectContaining(guitarOrder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGuitarOrder>>();
      const guitarOrder = { id: 123 };
      jest.spyOn(guitarOrderFormService, 'getGuitarOrder').mockReturnValue({ id: null });
      jest.spyOn(guitarOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ guitarOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: guitarOrder }));
      saveSubject.complete();

      // THEN
      expect(guitarOrderFormService.getGuitarOrder).toHaveBeenCalled();
      expect(guitarOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGuitarOrder>>();
      const guitarOrder = { id: 123 };
      jest.spyOn(guitarOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ guitarOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(guitarOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGuitar', () => {
      it('Should forward to guitarService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(guitarService, 'compareGuitar');
        comp.compareGuitar(entity, entity2);
        expect(guitarService.compareGuitar).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
