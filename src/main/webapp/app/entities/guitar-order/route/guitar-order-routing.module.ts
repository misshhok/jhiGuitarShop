import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GuitarOrderComponent } from '../list/guitar-order.component';
import { GuitarOrderDetailComponent } from '../detail/guitar-order-detail.component';
import { GuitarOrderUpdateComponent } from '../update/guitar-order-update.component';
import { GuitarOrderRoutingResolveService } from './guitar-order-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const guitarOrderRoute: Routes = [
  {
    path: '',
    component: GuitarOrderComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GuitarOrderDetailComponent,
    resolve: {
      guitarOrder: GuitarOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GuitarOrderUpdateComponent,
    resolve: {
      guitarOrder: GuitarOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GuitarOrderUpdateComponent,
    resolve: {
      guitarOrder: GuitarOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(guitarOrderRoute)],
  exports: [RouterModule],
})
export class GuitarOrderRoutingModule {}
