import { TestBed } from '@angular/core/testing';

import { IndiaStatsService } from './india-stats.service';

describe('IndiaStatsService', () => {
  let service: IndiaStatsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IndiaStatsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
