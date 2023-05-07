import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { GuitarTypeFormService, GuitarTypeFormGroup } from './guitar-type-form.service';
import { IGuitarType } from '../guitar-type.model';
import { GuitarTypeService } from '../service/guitar-type.service';

@Component({
  selector: 'jhi-guitar-type-update',
  templateUrl: './guitar-type-update.component.html',
})
export class GuitarTypeUpdateComponent implements OnInit {
  isSaving = false;
  guitarType: IGuitarType | null = null;

  editForm: GuitarTypeFormGroup = this.guitarTypeFormService.createGuitarTypeFormGroup();

  constructor(
    protected guitarTypeService: GuitarTypeService,
    protected guitarTypeFormService: GuitarTypeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guitarType }) => {
      this.guitarType = guitarType;
      if (guitarType) {
        this.updateForm(guitarType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const guitarType = this.guitarTypeFormService.getGuitarType(this.editForm);
    if (guitarType.id !== null) {
      this.subscribeToSaveResponse(this.guitarTypeService.update(guitarType));
    } else {
      this.subscribeToSaveResponse(this.guitarTypeService.create(guitarType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGuitarType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(guitarType: IGuitarType): void {
    this.guitarType = guitarType;
    this.guitarTypeFormService.resetForm(this.editForm, guitarType);
  }
}
