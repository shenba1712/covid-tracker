import {Component, Input, OnInit} from '@angular/core';
import {Resources} from "../../../domain/india/resources";

@Component({
  selector: 'app-terminal',
  templateUrl: './terminal.component.html',
  styleUrls: ['./terminal.component.scss']
})
export class TerminalComponent implements OnInit {

  @Input() resource: Resources;

  constructor() { }

  ngOnInit(): void {
  }

}
