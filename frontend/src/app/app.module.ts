import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { StatsCounterComponent } from './components/shared/stats-counter/stats-counter.component';
import { RocketSmokeComponent } from './components/shared/rocket-smoke/rocket-smoke.component';
import { TerminalComponent } from './components/shared/terminal/terminal.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { GlobalStatsService } from "./services/global/global-stats.service";
import { HttpClientModule } from "@angular/common/http";
import { SearchBarComponent } from './components/shared/search-bar/search-bar.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { CountriesCounterComponent } from './components/country/countries-counter/countries-counter.component';
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatCardModule } from "@angular/material/card";
import { CountryComponent } from './components/country/country-details/country.component';
import { IndianStatesCounterComponent } from './components/indian-state/indian-states-counter/indian-states-counter.component';
import { IndiaStatsService } from "./services/india/india-stats.service";
import { IndianStateComponent } from './components/indian-state/indian-state-details/indian-state.component';
import { IndianDistrictCounterComponent } from './components/indian-state/indian-district-counter/indian-district-counter.component';
import { IndianDistrictDetailsComponent } from './components/indian-state/indian-district-details/indian-district-details.component';
import { FormsModule } from "@angular/forms";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { ChartComponent } from './components/shared/chart/chart.component';
import { ResourcesMapComponent } from './components/indian-state/resources-map/resources-map.component';
import { NotFoundComponent } from './components/shared/not-found/not-found.component';
import { MatExpansionModule } from "@angular/material/expansion";
import { RouterModule } from '@angular/router';
import {MatTabsModule} from "@angular/material/tabs";

@NgModule({
  declarations: [
    AppComponent,
    StatsCounterComponent,
    RocketSmokeComponent,
    TerminalComponent,
    FooterComponent,
    DashboardComponent,
    SearchBarComponent,
    CountriesCounterComponent,
    CountryComponent,
    IndianStatesCounterComponent,
    IndianStateComponent,
    IndianDistrictCounterComponent,
    IndianDistrictDetailsComponent,
    ChartComponent,
    ResourcesMapComponent,
    NotFoundComponent
  ],
    imports: [
        BrowserModule.withServerTransition({appId: 'serverApp'}),
        AppRoutingModule,
        HttpClientModule,
        BrowserAnimationsModule,
        MatToolbarModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatButtonModule,
        MatPaginatorModule,
        MatCardModule,
        FormsModule,
        MatProgressSpinnerModule,
        MatExpansionModule,
        RouterModule,
        MatTabsModule
    ],
  providers: [
    GlobalStatsService,
    IndiaStatsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
