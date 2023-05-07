import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGuitarType, NewGuitarType } from '../guitar-type.model';

export type PartialUpdateGuitarType = Partial<IGuitarType> & Pick<IGuitarType, 'id'>;

export type EntityResponseType = HttpResponse<IGuitarType>;
export type EntityArrayResponseType = HttpResponse<IGuitarType[]>;

@Injectable({ providedIn: 'root' })
export class GuitarTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/guitar-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(guitarType: NewGuitarType): Observable<EntityResponseType> {
    return this.http.post<IGuitarType>(this.resourceUrl, guitarType, { observe: 'response' });
  }

  update(guitarType: IGuitarType): Observable<EntityResponseType> {
    return this.http.put<IGuitarType>(`${this.resourceUrl}/${this.getGuitarTypeIdentifier(guitarType)}`, guitarType, {
      observe: 'response',
    });
  }

  partialUpdate(guitarType: PartialUpdateGuitarType): Observable<EntityResponseType> {
    return this.http.patch<IGuitarType>(`${this.resourceUrl}/${this.getGuitarTypeIdentifier(guitarType)}`, guitarType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGuitarType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGuitarType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGuitarTypeIdentifier(guitarType: Pick<IGuitarType, 'id'>): number {
    return guitarType.id;
  }

  compareGuitarType(o1: Pick<IGuitarType, 'id'> | null, o2: Pick<IGuitarType, 'id'> | null): boolean {
    return o1 && o2 ? this.getGuitarTypeIdentifier(o1) === this.getGuitarTypeIdentifier(o2) : o1 === o2;
  }

  addGuitarTypeToCollectionIfMissing<Type extends Pick<IGuitarType, 'id'>>(
    guitarTypeCollection: Type[],
    ...guitarTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const guitarTypes: Type[] = guitarTypesToCheck.filter(isPresent);
    if (guitarTypes.length > 0) {
      const guitarTypeCollectionIdentifiers = guitarTypeCollection.map(guitarTypeItem => this.getGuitarTypeIdentifier(guitarTypeItem)!);
      const guitarTypesToAdd = guitarTypes.filter(guitarTypeItem => {
        const guitarTypeIdentifier = this.getGuitarTypeIdentifier(guitarTypeItem);
        if (guitarTypeCollectionIdentifiers.includes(guitarTypeIdentifier)) {
          return false;
        }
        guitarTypeCollectionIdentifiers.push(guitarTypeIdentifier);
        return true;
      });
      return [...guitarTypesToAdd, ...guitarTypeCollection];
    }
    return guitarTypeCollection;
  }
}
