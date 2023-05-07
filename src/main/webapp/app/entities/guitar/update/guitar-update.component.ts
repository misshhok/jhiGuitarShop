import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { GuitarFormService, GuitarFormGroup } from './guitar-form.service';
import { IGuitar } from '../guitar.model';
import { GuitarService } from '../service/guitar.service';
import { IGuitarType } from 'app/entities/guitar-type/guitar-type.model';
import { GuitarTypeService } from 'app/entities/guitar-type/service/guitar-type.service';

@Component({
  selector: 'jhi-guitar-update',
  templateUrl: './guitar-update.component.html',
})
export class GuitarUpdateComponent implements OnInit {
  isSaving = false;
  guitar: IGuitar | null = null;

  guitarTypesSharedCollection: IGuitarType[] = [];

  editForm: GuitarFormGroup = this.guitarFormService.createGuitarFormGroup();

  constructor(
    protected guitarService: GuitarService,
    protected guitarFormService: GuitarFormService,
    protected guitarTypeService: GuitarTypeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareGuitarType = (o1: IGuitarType | null, o2: IGuitarType | null): boolean => this.guitarTypeService.compareGuitarType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guitar }) => {
      this.guitar = guitar;
      if (guitar) {
        this.updateForm(guitar);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const guitar = this.guitarFormService.getGuitar(this.editForm);
    if (guitar.id !== null) {
      this.subscribeToSaveResponse(this.guitarService.update(guitar));
    } else {
      this.subscribeToSaveResponse(this.guitarService.create(guitar));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGuitar>>): void {
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

  protected updateForm(guitar: IGuitar): void {
    this.guitar = guitar;
    this.guitarFormService.resetForm(this.editForm, guitar);

    this.guitarTypesSharedCollection = this.guitarTypeService.addGuitarTypeToCollectionIfMissing<IGuitarType>(
      this.guitarTypesSharedCollection,
      guitar.guitarType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.guitarTypeService
      .query()
      .pipe(map((res: HttpResponse<IGuitarType[]>) => res.body ?? []))
      .pipe(
        map((guitarTypes: IGuitarType[]) =>
          this.guitarTypeService.addGuitarTypeToCollectionIfMissing<IGuitarType>(guitarTypes, this.guitar?.guitarType)
        )
      )
      .subscribe((guitarTypes: IGuitarType[]) => (this.guitarTypesSharedCollection = guitarTypes));
  }
}
