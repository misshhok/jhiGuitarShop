import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { GuitarOrderFormService, GuitarOrderFormGroup } from './guitar-order-form.service';
import { IGuitarOrder } from '../guitar-order.model';
import { GuitarOrderService } from '../service/guitar-order.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IGuitar } from 'app/entities/guitar/guitar.model';
import { GuitarService } from 'app/entities/guitar/service/guitar.service';

@Component({
  selector: 'jhi-guitar-order-update',
  templateUrl: './guitar-order-update.component.html',
})
export class GuitarOrderUpdateComponent implements OnInit {
  isSaving = false;
  guitarOrder: IGuitarOrder | null = null;

  usersSharedCollection: IUser[] = [];
  guitarsSharedCollection: IGuitar[] = [];

  editForm: GuitarOrderFormGroup = this.guitarOrderFormService.createGuitarOrderFormGroup();

  constructor(
    protected guitarOrderService: GuitarOrderService,
    protected guitarOrderFormService: GuitarOrderFormService,
    protected userService: UserService,
    protected guitarService: GuitarService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareGuitar = (o1: IGuitar | null, o2: IGuitar | null): boolean => this.guitarService.compareGuitar(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guitarOrder }) => {
      this.guitarOrder = guitarOrder;
      if (guitarOrder) {
        this.updateForm(guitarOrder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const guitarOrder = this.guitarOrderFormService.getGuitarOrder(this.editForm);
    if (guitarOrder.id !== null) {
      this.subscribeToSaveResponse(this.guitarOrderService.update(guitarOrder));
    } else {
      this.subscribeToSaveResponse(this.guitarOrderService.create(guitarOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGuitarOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(guitarOrder: IGuitarOrder): void {
    this.guitarOrder = guitarOrder;
    this.guitarOrderFormService.resetForm(this.editForm, guitarOrder);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, guitarOrder.orderOwner);
    this.guitarsSharedCollection = this.guitarService.addGuitarToCollectionIfMissing<IGuitar>(
      this.guitarsSharedCollection,
      ...(guitarOrder.guitarsInOrders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.guitarOrder?.orderOwner)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.guitarService
      .query()
      .pipe(map((res: HttpResponse<IGuitar[]>) => res.body ?? []))
      .pipe(
        map((guitars: IGuitar[]) =>
          this.guitarService.addGuitarToCollectionIfMissing<IGuitar>(guitars, ...(this.guitarOrder?.guitarsInOrders ?? []))
        )
      )
      .subscribe((guitars: IGuitar[]) => (this.guitarsSharedCollection = guitars));
  }
}
