import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CountriesCounterComponent } from './countries-counter.component';

describe('CountriesCounterComponent', () => {
  let component: CountriesCounterComponent;
  let fixture: ComponentFixture<CountriesCounterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CountriesCounterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CountriesCounterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
