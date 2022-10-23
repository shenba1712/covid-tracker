import {Component, OnInit} from '@angular/core';
import {GlobalStatsService} from "../../services/global/global-stats.service";
import {GlobalStats} from "../../domain/global-stats";
import {Country} from "../../domain/country";
import {ChartObject} from "../../domain/base/chart-object";
import * as introJs from 'intro.js/intro.js';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  globalStats: GlobalStats;
  countries: Country[];
  allCountries: Country[];
  fullChartData: ChartObject[];
  chartData: ChartObject;
  introJS = introJs();

  constructor(private globalStatsService: GlobalStatsService) {}

  ngOnInit(): void {
    this.globalStatsService.getGlobalStats().subscribe(stats => {
      this.globalStats = stats;
    });
    this.globalStatsService.getAllCountriesStats().subscribe(countries => {
      this.countries = this.allCountries = countries;
    });
    this.generateData();
  }

  onQueryChange(value: string) {
    if (!value.trim()) {
      this.countries = this.allCountries;
    } else {
      this.countries = this.allCountries.filter(country => {
        return country.countryName.toLowerCase().includes(value.trim().toLowerCase());
      });
    }
  }

  generateData() {
    this.globalStatsService.getChartData().subscribe(data => {
      this.fullChartData = data;
    });
  }

  getChart(statName) {
    this.chartData = this.fullChartData.filter(c => {
      return c.statName === statName;
    })[0];
  }

  startTour() {
    setTimeout( () => {
      this.introJS.setOptions({
        steps: [
          {
            element: '#step1',
            intro: 'Click on these buttons to get a visual representation of the data',
            position: 'top'
          },
          {
            element: '#step2',
            intro: 'Search for countries to get stats about the place.',
            position: 'right'
          },
          {
            element: '#step3',
            intro: 'Click on More Info to get detailed and graphical representation of the statistics of the country',
            position: 'bottom'
          }
        ].filter(obj => {
          const el = <HTMLElement>document.querySelector(obj.element);
          return el && el.offsetParent;
        }),
        exitOnOverlayClick: true,
        showProgress: false,
        showStepNumbers: false,
        doneLabel: "Done",
        nextLabel: "Next",
        prevLabel: "Previous",
        skipLabel: "Skip",
        hideNext: true,
        disableInteration: true,
        helperElementPadding: 15
      });

      this.introJS.start().onbeforechange(() => {
        window.scrollTo(0, 0);
      });
    }, 0);

  }

}
