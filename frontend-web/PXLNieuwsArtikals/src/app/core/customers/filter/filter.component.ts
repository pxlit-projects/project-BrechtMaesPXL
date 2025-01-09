import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core'; // Voor native datumondersteuning
import { Filter } from '../../../shared/models/filter.model';

@Component({
  selector: 'app-filter',
  standalone: true,
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css'],
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
  ],
})
export class FilterComponent {
  filter: Filter = { content: '', editorsId: '', date: null };

  @Output() filterChanged = new EventEmitter<Filter>();

  onSubmit(form: any): void {
    if (form.valid) {
      this.filterChanged.emit(this.filter);
    }  }
}
