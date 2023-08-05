import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Seller } from "../../domain/seller";
import { SellerForm } from "../../domain/forms/seller-form";
import { SellerRequest } from "../../domain/requests/seller-request";

@Component({
  selector: 'seller-create-edit',
  templateUrl: './seller-create-edit.component.html',
  styleUrls: ['./seller-create-edit.component.css']
})
export class SellerCreateEditComponent implements OnInit, OnChanges {
  @Input() seller: Seller | null = null;
  @Input() isMobile = false;
  @Output() submit = new EventEmitter<SellerRequest>();
  sellerForm: FormGroup<SellerForm>;
  error: string | null = null;

  constructor() {
    this.sellerForm = new FormGroup<SellerForm>({
      name: new FormControl('', { nonNullable: true, validators: Validators.required }),
      socialSecurityNumber: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.pattern("^[0-9]*$"), Validators.minLength(8), Validators.maxLength(10)]
      }),
      address: new FormControl(null),
      phoneNumber: new FormControl(null),
      email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
      additionalInfo: new FormControl(null)
    });
  }

  ngOnInit() {
    this.patchFormValue()
  }

  ngOnChanges() {
    this.patchFormValue()
  }

  patchFormValue() {
    if (this.seller) {
      this.sellerForm.controls.name.patchValue(this.seller.name);
      this.sellerForm.controls.socialSecurityNumber.patchValue(this.seller.socialSecurityNumber);
      this.sellerForm.controls.address.patchValue(this.seller.address);
      this.sellerForm.controls.phoneNumber.patchValue(this.seller.phoneNumber);
      this.sellerForm.controls.email.patchValue(this.seller.email);
      this.sellerForm.controls.additionalInfo.patchValue(this.seller.additionalInfo);
    }
  }

  onSubmit() {
    if (this.sellerForm.valid) {
      this.error = null;
      this.submit.emit(this.sellerForm.getRawValue());
    } else {
      this.error = 'required_fields_error';
    }
  }
}
