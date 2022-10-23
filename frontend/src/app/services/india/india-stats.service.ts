import { Injectable } from '@angular/core';
import {BackendRoutesConfig} from "../../domain/base/backend-routes.config";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {State} from "../../domain/india/state";
import {District} from "../../domain/india/district";
import {Resources} from "../../domain/india/resources";
import {ChartObject} from "../../domain/base/chart-object";

@Injectable()
export class IndiaStatsService {
  private baseUrl: string;

  constructor(private http: HttpClient) {
    this.baseUrl = BackendRoutesConfig.baseUrl + BackendRoutesConfig.india.baseUrl;
  }

  public getAllStatesStats(): Observable<State[]> {
    return this.http.get<State[]>(this.baseUrl + BackendRoutesConfig.india.states);
  }

  public getStateStats(name: string): Observable<State> {
    return this.http.get<State>(this.baseUrl + BackendRoutesConfig.india.state.replace('{name}', name));
  }

  public getDistrictStats(stateName: string, districtName: string): Observable<District> {
    return this.http.get<District>(this.baseUrl + BackendRoutesConfig.india.district
      .replace('{stateName}', stateName)
      .replace('{districtName}', districtName)
    );
  }

  public getResources(stateName: string, districtName?: string): Observable<Resources[]> {
    let link = this.baseUrl + BackendRoutesConfig.india.resources + "?stateName=" + stateName;
    if (!!districtName) {
      link = link + "&districtName=" + districtName;
    }
    return this.http.get<Resources[]>(link);
  }

  public getStateChartData(stateName: string, district?: string): Observable<ChartObject[]> {
    let url = this.baseUrl + BackendRoutesConfig.india.chartInfo +'?state=' + stateName;
    if (!!district) {
      url = url + "&district=" + district;
    }
    return this.http.get<ChartObject[]>(url);
  }
}
