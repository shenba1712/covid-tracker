import {Component, Input} from '@angular/core';
import {Router} from "@angular/router";
import {State} from "../../../domain/india/state";

@Component({
  selector: 'app-indian-states-counter',
  templateUrl: './indian-states-counter.component.html',
  styleUrls: ['./indian-states-counter.component.scss']
})
export class IndianStatesCounterComponent {
  states: State[];
  pagedList: State[];

  @Input() set setStates(value) {
    if (value) {
      this.states = value;
      this.pagedList = value.slice(0, 15);
    }
  }

  constructor(private router: Router) { }

  pageEvent(event) {
    let startIndex = event.pageIndex * event.pageSize;
    let endIndex = startIndex + event.pageSize;
    if(endIndex > this.states.length){
      endIndex = this.states.length;
    }
    this.pagedList = this.states.slice(startIndex, endIndex);
  }

  getMoreInfo(name: string) {
    this.router.navigate(['state', name.toLowerCase()]);
  }

}
