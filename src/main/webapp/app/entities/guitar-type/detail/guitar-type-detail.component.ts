import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGuitarType } from '../guitar-type.model';

@Component({
  selector: 'jhi-guitar-type-detail',
  templateUrl: './guitar-type-detail.component.html',
})
export class GuitarTypeDetailComponent implements OnInit {
  guitarType: IGuitarType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guitarType }) => {
      this.guitarType = guitarType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
