import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGuitar } from '../guitar.model';
import { GuitarService } from '../service/guitar.service';

@Injectable({ providedIn: 'root' })
export class GuitarRoutingResolveService implements Resolve<IGuitar | null> {
  constructor(protected service: GuitarService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGuitar | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((guitar: HttpResponse<IGuitar>) => {
          if (guitar.body) {
            return of(guitar.body);
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
