import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'guitar-type',
        data: { pageTitle: 'guitarShopApp.guitarType.home.title' },
        loadChildren: () => import('./guitar-type/guitar-type.module').then(m => m.GuitarTypeModule),
      },
      {
        path: 'guitar',
        data: { pageTitle: 'guitarShopApp.guitar.home.title' },
        loadChildren: () => import('./guitar/guitar.module').then(m => m.GuitarModule),
      },
      {
        path: 'guitar-order',
        data: { pageTitle: 'guitarShopApp.guitarOrder.home.title' },
        loadChildren: () => import('./guitar-order/guitar-order.module').then(m => m.GuitarOrderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
