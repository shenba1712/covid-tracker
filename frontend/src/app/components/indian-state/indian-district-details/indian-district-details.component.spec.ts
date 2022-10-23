import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IndianDistrictDetailsComponent } from './indian-district-details.component';

describe('IndianDistrictDetailsComponent', () => {
  let component: IndianDistrictDetailsComponent;
  let fixture: ComponentFixture<IndianDistrictDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IndianDistrictDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IndianDistrictDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
