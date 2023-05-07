import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGuitarType } from '../guitar-type.model';
import { GuitarTypeService } from '../service/guitar-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './guitar-type-delete-dialog.component.html',
})
export class GuitarTypeDeleteDialogComponent {
  guitarType?: IGuitarType;

  constructor(protected guitarTypeService: GuitarTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.guitarTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
