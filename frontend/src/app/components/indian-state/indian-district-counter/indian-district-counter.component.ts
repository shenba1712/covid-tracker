import {Component, Input} from '@angular/core';
import {District} from "../../../domain/india/district";
import {Router} from "@angular/router";

@Component({
  selector: 'app-indian-district-counter',
  templateUrl: './indian-district-counter.component.html',
  styleUrls: ['./indian-district-counter.component.scss']
})
export class IndianDistrictCounterComponent {
  districts: District[];
  pagedList: District[];

  @Input() set setDistricts(value) {
    if (value) {
      this.districts = value;
      this.pagedList = value.slice(0, 15);
    }
  }

  @Input() state: string;

  constructor(private router: Router) { }

  pageEvent(event) {
    let startIndex = event.pageIndex * event.pageSize;
    let endIndex = startIndex + event.pageSize;
    if(endIndex > this.districts.length){
      endIndex = this.districts.length;
    }
    this.pagedList = this.districts.slice(startIndex, endIndex);
  }

  getMoreInfo(name: string) {
    this.router.navigate(['state', this.state.toLowerCase() ,'district', name.toLowerCase()]);
  }

  setBorder(zone: string, base: string) {
    if (zone === 'Red') {
      return base + '-danger';
    } else if (zone === 'Green') {
      return base + '-success';
    } else if (zone === 'Orange') {
     return base + '-warning'
    }
    return '';
  }

}
