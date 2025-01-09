import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DraftArtilceListComponent } from './draft-artilce-list.component';

describe('DraftArtilceListComponent', () => {
  let component: DraftArtilceListComponent;
  let fixture: ComponentFixture<DraftArtilceListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DraftArtilceListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DraftArtilceListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
