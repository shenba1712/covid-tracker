import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TimeSeries} from "../../../domain/base/time-series";

@Component({
  selector: 'app-stats-counter',
  templateUrl: './stats-counter.component.html',
  styleUrls: ['./stats-counter.component.scss']
})
export class StatsCounterComponent {

  @Input() stats: TimeSeries;
  @Input() isIndia = false;

  @Output() statName = new EventEmitter<string>();

  active = {
    confirmed: false,
    active: false,
    deceased: false,
    recovered: false
  }

  constructor() { }

  setRecoveredClass(value: number, active: boolean): string {
    let baseClass = active ? 'bg' : 'border';
    if (value > 0) {
      return baseClass + '-success';
    } else if (value < 0) {
      return baseClass + '-danger';
    }
      return baseClass + '-warning';
  }

  setRecoveredBadgeClass(value: number): string {
    if (value > 0) {
      return 'badge-success';
    } else if (value < 0) {
      return 'badge-danger';
    }
    return 'badge-warning';
  }

  // Confirmed, Deceased and Active
  setClass(value: number, active: boolean): string {
    let baseClass = active ? 'bg' : 'border';
    if (value <= 0) {
      return baseClass + '-success';
    } else {
      return baseClass + '-danger';
    }
  }

  getChartData(statName: string) {
    for (let activeKey in this.active) {
      this.active[activeKey] =  activeKey === statName;
    }
    this.statName.emit(statName);
  }

}
