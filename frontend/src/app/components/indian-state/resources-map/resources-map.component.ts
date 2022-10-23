import {Component, Input, OnInit} from '@angular/core';
import {IndiaStatsService} from "../../../services/india/india-stats.service";
import {Resources} from "../../../domain/india/resources";

@Component({
  selector: 'app-resources-map',
  templateUrl: './resources-map.component.html',
  styleUrls: ['./resources-map.component.scss']
})

export class ResourcesMapComponent implements OnInit {

  private _districtName: string;

  get districtName(): string {
    return this._districtName;
  }

  @Input() set districtName(districtName) {
    if (districtName) {
      this._districtName = districtName;
    }
  }
  @Input() stateName: string;

  resources: Resources[];

  constructor(private service: IndiaStatsService) {}

  ngOnInit() {
    this.service.getResources(this.stateName, this._districtName).subscribe(resources => {
      this.resources = resources;
    });
  }
}
