import { Injectable } from '@angular/core';
import {BackendRoutesConfig} from "../../domain/base/backend-routes.config";
import {Observable} from "rxjs";
import {GlobalStats} from "../../domain/global-stats";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Country} from "../../domain/country";
import {ChartObject} from "../../domain/base/chart-object";

@Injectable()
export class GlobalStatsService {
  private baseUrl: string;

  constructor(private http: HttpClient) {
    this.baseUrl = BackendRoutesConfig.baseUrl + BackendRoutesConfig.global.baseUrl;
  }

  public getGlobalStats(): Observable<GlobalStats> {
    return this.http.get<GlobalStats>(this.baseUrl + BackendRoutesConfig.global.latest);
  }

  public getAllCountriesStats(): Observable<Country[]> {
    return this.http.get<Country[]>(this.baseUrl + BackendRoutesConfig.global.countries);
  }

  public getCountryStats(country: string): Observable<Country> {
    return this.http.get<Country>(this.baseUrl + BackendRoutesConfig.global.country.replace('{name}', country));
  }

  public getChartData(countryName?: string): Observable<ChartObject[]> {
    let url = this.baseUrl + BackendRoutesConfig.global.chartInfo;

    if (!!countryName) {
      url += '?country=' + countryName;
    }
    return this.http.get<ChartObject[]>(url);
  }

}
