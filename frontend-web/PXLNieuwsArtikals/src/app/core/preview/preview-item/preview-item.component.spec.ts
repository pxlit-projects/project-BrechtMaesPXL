import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewItemComponent } from './preview-item.component';

describe('PreviewItemComponent', () => {
  let component: PreviewItemComponent;
  let fixture: ComponentFixture<PreviewItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PreviewItemComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PreviewItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
