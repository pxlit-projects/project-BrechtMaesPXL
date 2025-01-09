import { TestBed } from '@angular/core/testing';

import { ReveiwService } from './reveiw.service';

describe('ReveiwService', () => {
  let service: ReveiwService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReveiwService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
