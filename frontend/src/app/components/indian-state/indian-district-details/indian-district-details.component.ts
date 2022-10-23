import { Component, OnInit } from '@angular/core';
import {District} from "../../../domain/india/district";
import {IndiaStatsService} from "../../../services/india/india-stats.service";
import {ActivatedRoute, Router} from "@angular/router";
import { Location } from '@angular/common';
import {ChartObject} from "../../../domain/base/chart-object";

@Component({
  selector: 'app-indian-district-details',
  templateUrl: './indian-district-details.component.html',
  styleUrls: ['./indian-district-details.component.scss']
})
export class IndianDistrictDetailsComponent implements OnInit {
  district: District;
  stateName: string;
  districtName: string;

  fullChartData: ChartObject[];
  chartData: ChartObject;

  constructor(private service: IndiaStatsService, private router: Router, private activatedRoute: ActivatedRoute,
              private _location: Location) { }

  ngOnInit(): void {
    this.stateName = this.activatedRoute.snapshot.params['stateName'];
    this.districtName = this.activatedRoute.snapshot.params['districtName'];
    this.service.getDistrictStats(this.stateName, this.districtName).subscribe(district => {
      if (!!district) {
        this.district = district;
      }
    }, error => {
      if (error.status === 404) {
        this.router.navigateByUrl('/not-found');
      }
    });
    this.generateData();
  }

  goBack() {
    this._location.back();
  }

  generateData() {
    this.service.getStateChartData(this.stateName, this.districtName).subscribe(data => {
      this.fullChartData = data;
    });
  }

  getChart(statName) {
    this.chartData = this.fullChartData.filter(c => {
      return c.statName === statName;
    })[0];
  }

}
