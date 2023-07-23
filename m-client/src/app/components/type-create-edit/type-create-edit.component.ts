import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Type } from "../../domain/type";
import { TypeForm } from "../../domain/forms/type-form";
import { TypeRequest } from "../../domain/requests/type-request";

@Component({
  selector: 'type-create-edit',
  templateUrl: './type-create-edit.component.html',
  styleUrls: ['./type-create-edit.component.css']
})
export class TypeCreateEditComponent implements OnInit, OnChanges {
  @Input() type: Type | null = null;
  @Input() isMobile = false;
  @Output() submit = new EventEmitter<TypeRequest>();
  typeForm: FormGroup<TypeForm>;

  error: string | null = null;

  constructor() {
    this.typeForm = new FormGroup<TypeForm>({
      name: new FormControl('', { nonNullable: true, validators: Validators.required }),
      description: new FormControl(null)
    });
  }

  ngOnInit() {
    if (this.type) {
      this.typeForm.controls.name.patchValue(this.type.name);
      this.typeForm.controls.description.patchValue(this.type.description);
    }
  }

  ngOnChanges() {
    if (this.type) {
      this.typeForm.controls.name.patchValue(this.type.name);
      this.typeForm.controls.description.patchValue(this.type.description);
    }
  }

  onSubmit() {
    if (this.typeForm.valid) {
      this.error = null;
      this.submit.emit(this.typeForm.getRawValue());
    } else {
      this.error = 'required_fields_error';
    }
  }
}
