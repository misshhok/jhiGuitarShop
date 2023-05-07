import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGuitar } from '../guitar.model';

@Component({
  selector: 'jhi-guitar-detail',
  templateUrl: './guitar-detail.component.html',
})
export class GuitarDetailComponent implements OnInit {
  guitar: IGuitar | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guitar }) => {
      this.guitar = guitar;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
