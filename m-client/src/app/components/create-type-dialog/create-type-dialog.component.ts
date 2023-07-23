import { Component, Inject, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { TypeRequest } from "../../domain/requests/type-request";
import { FormControl, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: 'create-type-dialog',
  templateUrl: './create-type-dialog.component.html',
  styleUrls: ['./create-type-dialog.component.css']
})
export class CreateTypeDialogComponent {
  typeForm: FormGroup;
  error: string | null = null;
  constructor(public dialogRef: MatDialogRef<CreateTypeDialogComponent>) {
    this.typeForm = new FormGroup({
      name: new FormControl('', Validators.required),
      description: new FormControl(null),
    });
  }

  ngOnInit(): void {}

  onSubmit() {
    if (this.typeForm.valid) {
      const request: TypeRequest = this.typeForm.value;
      this.dialogRef.close(request);
    } else {
      this.error = 'required_fields_error';
    }
  }
}
