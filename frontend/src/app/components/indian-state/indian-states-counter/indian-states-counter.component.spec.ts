import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndianStatesCounterComponent } from './indian-states-counter.component';

describe('IndianStatesCounterComponent', () => {
  let component: IndianStatesCounterComponent;
  let fixture: ComponentFixture<IndianStatesCounterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IndianStatesCounterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndianStatesCounterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
