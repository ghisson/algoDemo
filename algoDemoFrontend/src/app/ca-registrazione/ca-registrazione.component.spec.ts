import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaRegistrazioneComponent } from './ca-registrazione.component';

describe('CaRegistrazioneComponent', () => {
  let component: CaRegistrazioneComponent;
  let fixture: ComponentFixture<CaRegistrazioneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CaRegistrazioneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CaRegistrazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
