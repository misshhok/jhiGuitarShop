import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGuitarOrder } from '../guitar-order.model';
import { GuitarOrderService } from '../service/guitar-order.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './guitar-order-delete-dialog.component.html',
})
export class GuitarOrderDeleteDialogComponent {
  guitarOrder?: IGuitarOrder;

  constructor(protected guitarOrderService: GuitarOrderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.guitarOrderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
