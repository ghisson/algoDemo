import { TestBed } from '@angular/core/testing';

import { ValutazioniService } from './valutazioni.service';

describe('ValutazioniService', () => {
  let service: ValutazioniService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ValutazioniService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
