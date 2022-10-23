import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {CountryComponent} from "./components/country/country-details/country.component";
import {IndianStateComponent} from "./components/indian-state/indian-state-details/indian-state.component";
import {IndianDistrictDetailsComponent} from "./components/indian-state/indian-district-details/indian-district-details.component";
import {NotFoundComponent} from "./components/shared/not-found/not-found.component";

const routes: Routes = [
  { path: 'dashboard', component: DashboardComponent},
  { path: 'country/:name', component: CountryComponent},
  { path: 'state/:name', component: IndianStateComponent},
  { path: 'state/:stateName/district/:districtName', component: IndianDistrictDetailsComponent},
  { path: 'not-found', component: NotFoundComponent},
  { path: '', redirectTo: '/dashboard', pathMatch: "full" },
  { path: '**', component: NotFoundComponent }
  ]
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
