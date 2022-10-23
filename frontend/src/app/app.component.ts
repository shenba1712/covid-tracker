import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Covid Tracker';
  today = new Date().toDateString();

  constructor(private router: Router) {
  }

  goToDashboard() {
    this.router.navigate(['/']);
  }

}
