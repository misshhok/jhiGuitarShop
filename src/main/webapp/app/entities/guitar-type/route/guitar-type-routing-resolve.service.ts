import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGuitarType } from '../guitar-type.model';
import { GuitarTypeService } from '../service/guitar-type.service';

@Injectable({ providedIn: 'root' })
export class GuitarTypeRoutingResolveService implements Resolve<IGuitarType | null> {
  constructor(protected service: GuitarTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGuitarType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((guitarType: HttpResponse<IGuitarType>) => {
          if (guitarType.body) {
            return of(guitarType.body);
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
