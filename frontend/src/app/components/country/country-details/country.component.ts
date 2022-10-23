import { Component, OnInit } from '@angular/core';
import {Country} from "../../../domain/country";
import {GlobalStatsService} from "../../../services/global/global-stats.service";
import {ActivatedRoute, Router} from "@angular/router";
import {IndiaStatsService} from "../../../services/india/india-stats.service";
import {State} from "../../../domain/india/state";
import {Location} from "@angular/common";
import {ChartObject} from "../../../domain/base/chart-object";

@Component({
  selector: 'app-country',
  templateUrl: './country.component.html',
  styleUrls: ['./country.component.scss']
})
export class CountryComponent implements OnInit {

  countryStats: Country;
  countryName: string;
  isIndia: boolean = false;
  states: State[];
  allStates: State[];

  fullChartData: ChartObject[];
  chartData: ChartObject;

  constructor(private service: GlobalStatsService, private indianStatsService: IndiaStatsService,
              private activatedRoute: ActivatedRoute, private router: Router,
              private _location: Location) { }

  ngOnInit(): void {
    this.countryName = this.activatedRoute.snapshot.params['name'];
    this.isIndia = this.countryName === 'india';
    this.service.getCountryStats(this.countryName).subscribe(stats => {
      if (!!stats) {
        this.countryStats = stats;
      }
    }, error => {
      if (error.status === 404) {
        this.router.navigateByUrl('/not-found');
      }
    });
    if (this.isIndia) {
      this.indianStatsService.getAllStatesStats().subscribe(states => {
        this.states = this.allStates = states;
      });
    }
    this.generateData();
  }

  onQueryChange(value: string) {
    if (!value.trim()) {
      this.states = this.allStates;
    } else {
      this.states = this.allStates.filter(state => {
        return state.stateName.toLowerCase().includes(value.trim().toLowerCase());
      });
    }
  }

  goBack() {
    this._location.back();
  }

  generateData() {
    this.service.getChartData(this.countryName).subscribe(data => {
      this.fullChartData = data;
    });
  }

  getChart(statName) {
    this.chartData = this.fullChartData?.filter(c => {
      return c.statName === statName;
    })[0];
  }

}
