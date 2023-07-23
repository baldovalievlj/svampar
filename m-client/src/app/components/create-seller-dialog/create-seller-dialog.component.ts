import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { TypeRequest } from "../../domain/requests/type-request";
import { SellerForm } from "../../domain/forms/seller-form";
import { Seller } from "../../domain/seller";
import { SellerRequest } from "../../domain/requests/seller-request";
import { TranslateService } from "@ngx-translate/core";

@Component({
  selector: 'app-create-seller-dialog',
  templateUrl: './create-seller-dialog.component.html',
  styleUrls: ['./create-seller-dialog.component.css']
})
export class CreateSellerDialogComponent {
  sellerForm: FormGroup<SellerForm>;
  error: string | null = null;
  constructor(public dialogRef: MatDialogRef<CreateSellerDialogComponent>) {
    this.sellerForm =new FormGroup<SellerForm>({
      name: new FormControl('', { nonNullable: true, validators: Validators.required }),
      socialSecurityNumber: new FormControl('', { nonNullable: true, validators: Validators.required }),
      address: new FormControl(null ),
      phoneNumber: new FormControl(null),
      email: new FormControl('', {nonNullable: true, validators: [Validators.required, Validators.email] }),
      additionalInfo: new FormControl(null)
    });
  }

  ngOnInit(): void {}

  onSubmit() {
    if (this.sellerForm.valid) {
      this.dialogRef.close(this.sellerForm.getRawValue());
    } else {
      this.error = 'required_fields_error';
    }
  }
}
