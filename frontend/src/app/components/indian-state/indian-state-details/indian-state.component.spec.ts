import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndianStateComponent } from './indian-state.component';

describe('IndianStateComponent', () => {
  let component: IndianStateComponent;
  let fixture: ComponentFixture<IndianStateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IndianStateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndianStateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
