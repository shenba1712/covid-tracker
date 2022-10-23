import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent {

  @Input() text: string = 'Type something to search...';
  @Output() query = new EventEmitter<string>();
  searchText: string;


  constructor() { }

  onSearch() {
    this.query.emit(this.searchText);
  }

}
