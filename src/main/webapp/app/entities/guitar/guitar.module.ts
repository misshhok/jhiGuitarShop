import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GuitarComponent } from './list/guitar.component';
import { GuitarDetailComponent } from './detail/guitar-detail.component';
import { GuitarUpdateComponent } from './update/guitar-update.component';
import { GuitarDeleteDialogComponent } from './delete/guitar-delete-dialog.component';
import { GuitarRoutingModule } from './route/guitar-routing.module';

@NgModule({
  imports: [SharedModule, GuitarRoutingModule],
  declarations: [GuitarComponent, GuitarDetailComponent, GuitarUpdateComponent, GuitarDeleteDialogComponent],
})
export class GuitarModule {}
