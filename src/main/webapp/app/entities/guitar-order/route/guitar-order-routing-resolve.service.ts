import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGuitarOrder } from '../guitar-order.model';
import { GuitarOrderService } from '../service/guitar-order.service';

@Injectable({ providedIn: 'root' })
export class GuitarOrderRoutingResolveService implements Resolve<IGuitarOrder | null> {
  constructor(protected service: GuitarOrderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGuitarOrder | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((guitarOrder: HttpResponse<IGuitarOrder>) => {
          if (guitarOrder.body) {
            return of(guitarOrder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
