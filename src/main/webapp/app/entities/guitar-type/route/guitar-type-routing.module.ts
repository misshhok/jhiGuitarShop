import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GuitarTypeComponent } from '../list/guitar-type.component';
import { GuitarTypeDetailComponent } from '../detail/guitar-type-detail.component';
import { GuitarTypeUpdateComponent } from '../update/guitar-type-update.component';
import { GuitarTypeRoutingResolveService } from './guitar-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const guitarTypeRoute: Routes = [
  {
    path: '',
    component: GuitarTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GuitarTypeDetailComponent,
    resolve: {
      guitarType: GuitarTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GuitarTypeUpdateComponent,
    resolve: {
      guitarType: GuitarTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GuitarTypeUpdateComponent,
    resolve: {
      guitarType: GuitarTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(guitarTypeRoute)],
  exports: [RouterModule],
})
export class GuitarTypeRoutingModule {}
