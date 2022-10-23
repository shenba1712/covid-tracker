import { Component, OnInit } from '@angular/core';
import {State} from "../../../domain/india/state";
import {District} from "../../../domain/india/district";
import {IndiaStatsService} from "../../../services/india/india-stats.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {ChartObject} from "../../../domain/base/chart-object";

@Component({
  selector: 'app-indian-state',
  templateUrl: './indian-state.component.html',
  styleUrls: ['./indian-state.component.scss']
})
export class IndianStateComponent implements OnInit {
  stateStats: State;
  stateName: string;
  districts: District[];
  allDistricts: District[];

  fullChartData: ChartObject[];
  chartData: ChartObject;

  constructor(private service: IndiaStatsService, private router: Router,
              private activatedRoute: ActivatedRoute, private _location: Location) { }

  ngOnInit(): void {
    this.stateName = this.activatedRoute.snapshot.params['name'];
    this.service.getStateStats(this.stateName).subscribe(stats => {
      if (!!stats) {
        this.stateStats = stats;
        this.districts = this.allDistricts = stats.districts;
      }
    }, error => {
      if (error.status === 404) {
        this.router.navigateByUrl('/not-found');
      }
    });
    this.generateData();
  }

  onQueryChange(value: string) {
    if (!value.trim()) {
      this.districts = this.allDistricts;
    } else {
      this.districts = this.allDistricts.filter(district => {
        return district.districtName.toLowerCase().includes(value.trim().toLowerCase());
      });
    }
  }

  goBack() {
    this._location.back();
  }

  generateData() {
    this.service.getStateChartData(this.stateName).subscribe(data => {
      this.fullChartData = data;
    });
  }

  getChart(statName) {
    this.chartData = this.fullChartData.filter(c => {
      return c.statName === statName;
    })[0];
  }

}
