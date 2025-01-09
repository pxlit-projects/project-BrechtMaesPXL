import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewArtilceItemComponent } from './review-artilce-item.component';

describe('ReviewArtilceItemComponent', () => {
  let component: ReviewArtilceItemComponent;
  let fixture: ComponentFixture<ReviewArtilceItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewArtilceItemComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewArtilceItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
