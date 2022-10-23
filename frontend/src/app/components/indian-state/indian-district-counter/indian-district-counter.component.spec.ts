import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndianDistrictCounterComponent } from './indian-district-counter.component';

describe('IndianDistrictCounterComponent', () => {
  let component: IndianDistrictCounterComponent;
  let fixture: ComponentFixture<IndianDistrictCounterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IndianDistrictCounterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndianDistrictCounterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
