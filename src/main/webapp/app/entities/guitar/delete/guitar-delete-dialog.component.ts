import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGuitar } from '../guitar.model';
import { GuitarService } from '../service/guitar.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './guitar-delete-dialog.component.html',
})
export class GuitarDeleteDialogComponent {
  guitar?: IGuitar;

  constructor(protected guitarService: GuitarService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.guitarService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
