import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GuitarTypeComponent } from './list/guitar-type.component';
import { GuitarTypeDetailComponent } from './detail/guitar-type-detail.component';
import { GuitarTypeUpdateComponent } from './update/guitar-type-update.component';
import { GuitarTypeDeleteDialogComponent } from './delete/guitar-type-delete-dialog.component';
import { GuitarTypeRoutingModule } from './route/guitar-type-routing.module';

@NgModule({
  imports: [SharedModule, GuitarTypeRoutingModule],
  declarations: [GuitarTypeComponent, GuitarTypeDetailComponent, GuitarTypeUpdateComponent, GuitarTypeDeleteDialogComponent],
})
export class GuitarTypeModule {}
