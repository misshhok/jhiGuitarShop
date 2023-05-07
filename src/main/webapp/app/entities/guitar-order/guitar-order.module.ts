import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GuitarOrderComponent } from './list/guitar-order.component';
import { GuitarOrderDetailComponent } from './detail/guitar-order-detail.component';
import { GuitarOrderUpdateComponent } from './update/guitar-order-update.component';
import { GuitarOrderDeleteDialogComponent } from './delete/guitar-order-delete-dialog.component';
import { GuitarOrderRoutingModule } from './route/guitar-order-routing.module';

@NgModule({
  imports: [SharedModule, GuitarOrderRoutingModule],
  declarations: [GuitarOrderComponent, GuitarOrderDetailComponent, GuitarOrderUpdateComponent, GuitarOrderDeleteDialogComponent],
})
export class GuitarOrderModule {}
