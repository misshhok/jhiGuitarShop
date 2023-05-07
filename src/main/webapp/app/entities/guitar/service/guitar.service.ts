import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGuitar, NewGuitar } from '../guitar.model';

export type PartialUpdateGuitar = Partial<IGuitar> & Pick<IGuitar, 'id'>;

export type EntityResponseType = HttpResponse<IGuitar>;
export type EntityArrayResponseType = HttpResponse<IGuitar[]>;

@Injectable({ providedIn: 'root' })
export class GuitarService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/guitars');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(guitar: NewGuitar): Observable<EntityResponseType> {
    return this.http.post<IGuitar>(this.resourceUrl, guitar, { observe: 'response' });
  }

  update(guitar: IGuitar): Observable<EntityResponseType> {
    return this.http.put<IGuitar>(`${this.resourceUrl}/${this.getGuitarIdentifier(guitar)}`, guitar, { observe: 'response' });
  }

  partialUpdate(guitar: PartialUpdateGuitar): Observable<EntityResponseType> {
    return this.http.patch<IGuitar>(`${this.resourceUrl}/${this.getGuitarIdentifier(guitar)}`, guitar, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGuitar>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGuitar[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGuitarIdentifier(guitar: Pick<IGuitar, 'id'>): number {
    return guitar.id;
  }

  compareGuitar(o1: Pick<IGuitar, 'id'> | null, o2: Pick<IGuitar, 'id'> | null): boolean {
    return o1 && o2 ? this.getGuitarIdentifier(o1) === this.getGuitarIdentifier(o2) : o1 === o2;
  }

  addGuitarToCollectionIfMissing<Type extends Pick<IGuitar, 'id'>>(
    guitarCollection: Type[],
    ...guitarsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const guitars: Type[] = guitarsToCheck.filter(isPresent);
    if (guitars.length > 0) {
      const guitarCollectionIdentifiers = guitarCollection.map(guitarItem => this.getGuitarIdentifier(guitarItem)!);
      const guitarsToAdd = guitars.filter(guitarItem => {
        const guitarIdentifier = this.getGuitarIdentifier(guitarItem);
        if (guitarCollectionIdentifiers.includes(guitarIdentifier)) {
          return false;
        }
        guitarCollectionIdentifiers.push(guitarIdentifier);
        return true;
      });
      return [...guitarsToAdd, ...guitarCollection];
    }
    return guitarCollection;
  }
}
