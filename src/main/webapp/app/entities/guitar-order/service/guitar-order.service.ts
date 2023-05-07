import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGuitarOrder, NewGuitarOrder } from '../guitar-order.model';

export type PartialUpdateGuitarOrder = Partial<IGuitarOrder> & Pick<IGuitarOrder, 'id'>;

export type EntityResponseType = HttpResponse<IGuitarOrder>;
export type EntityArrayResponseType = HttpResponse<IGuitarOrder[]>;

@Injectable({ providedIn: 'root' })
export class GuitarOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/guitar-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(guitarOrder: NewGuitarOrder): Observable<EntityResponseType> {
    return this.http.post<IGuitarOrder>(this.resourceUrl, guitarOrder, { observe: 'response' });
  }

  update(guitarOrder: IGuitarOrder): Observable<EntityResponseType> {
    return this.http.put<IGuitarOrder>(`${this.resourceUrl}/${this.getGuitarOrderIdentifier(guitarOrder)}`, guitarOrder, {
      observe: 'response',
    });
  }

  partialUpdate(guitarOrder: PartialUpdateGuitarOrder): Observable<EntityResponseType> {
    return this.http.patch<IGuitarOrder>(`${this.resourceUrl}/${this.getGuitarOrderIdentifier(guitarOrder)}`, guitarOrder, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGuitarOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGuitarOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGuitarOrderIdentifier(guitarOrder: Pick<IGuitarOrder, 'id'>): number {
    return guitarOrder.id;
  }

  compareGuitarOrder(o1: Pick<IGuitarOrder, 'id'> | null, o2: Pick<IGuitarOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getGuitarOrderIdentifier(o1) === this.getGuitarOrderIdentifier(o2) : o1 === o2;
  }

  addGuitarOrderToCollectionIfMissing<Type extends Pick<IGuitarOrder, 'id'>>(
    guitarOrderCollection: Type[],
    ...guitarOrdersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const guitarOrders: Type[] = guitarOrdersToCheck.filter(isPresent);
    if (guitarOrders.length > 0) {
      const guitarOrderCollectionIdentifiers = guitarOrderCollection.map(
        guitarOrderItem => this.getGuitarOrderIdentifier(guitarOrderItem)!
      );
      const guitarOrdersToAdd = guitarOrders.filter(guitarOrderItem => {
        const guitarOrderIdentifier = this.getGuitarOrderIdentifier(guitarOrderItem);
        if (guitarOrderCollectionIdentifiers.includes(guitarOrderIdentifier)) {
          return false;
        }
        guitarOrderCollectionIdentifiers.push(guitarOrderIdentifier);
        return true;
      });
      return [...guitarOrdersToAdd, ...guitarOrderCollection];
    }
    return guitarOrderCollection;
  }
}
