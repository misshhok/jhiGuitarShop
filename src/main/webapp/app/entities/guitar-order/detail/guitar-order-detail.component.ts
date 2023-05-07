import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGuitarOrder } from '../guitar-order.model';

@Component({
  selector: 'jhi-guitar-order-detail',
  templateUrl: './guitar-order-detail.component.html',
})
export class GuitarOrderDetailComponent implements OnInit {
  guitarOrder: IGuitarOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guitarOrder }) => {
      this.guitarOrder = guitarOrder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
