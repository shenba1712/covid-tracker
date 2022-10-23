import {Component, Input} from '@angular/core';
import {Country} from "../../../domain/country";
import {Router} from "@angular/router";

@Component({
  selector: 'app-countries-counter',
  templateUrl: './countries-counter.component.html',
  styleUrls: ['./countries-counter.component.scss']
})
export class CountriesCounterComponent {

  countries: Country[];

  @Input() set setCountries(value: Country[]) {
    if (value) { //null check
      this.countries = value;
      this.pagedList = value.slice(0,15); //setting default list with 15 values
    }
  }
  pagedList: Country[];

  constructor(private router: Router) { }

  pageEvent(event) {
    let startIndex = event.pageIndex * event.pageSize;
    let endIndex = startIndex + event.pageSize;
    if(endIndex > this.countries.length){
      endIndex = this.countries.length;
    }
    this.pagedList = this.countries.slice(startIndex, endIndex);
  }

  getMoreInfo(name: string) {
    this.router.navigate(['country', name.toLowerCase()]);
  }

}
