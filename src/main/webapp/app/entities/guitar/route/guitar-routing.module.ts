import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GuitarComponent } from '../list/guitar.component';
import { GuitarDetailComponent } from '../detail/guitar-detail.component';
import { GuitarUpdateComponent } from '../update/guitar-update.component';
import { GuitarRoutingResolveService } from './guitar-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const guitarRoute: Routes = [
  {
    path: '',
    component: GuitarComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GuitarDetailComponent,
    resolve: {
      guitar: GuitarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GuitarUpdateComponent,
    resolve: {
      guitar: GuitarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GuitarUpdateComponent,
    resolve: {
      guitar: GuitarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(guitarRoute)],
  exports: [RouterModule],
})
export class GuitarRoutingModule {}
