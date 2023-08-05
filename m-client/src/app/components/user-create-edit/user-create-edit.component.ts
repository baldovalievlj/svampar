import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, NgForm, Validators } from "@angular/forms";
import { UserForm } from "../../domain/forms/user-form";
import { User } from "../../domain/user";
import { Role } from "../../domain/role";
import { confirmPasswordValidator } from "../../services/validators/confirm-password-validator";
import { UserRequest } from "../../domain/requests/user-request";

@Component({
  selector: 'user-create-edit',
  templateUrl: './user-create-edit.component.html',
  styleUrls: ['./user-create-edit.component.css'],
})
export class UserCreateEditComponent implements OnInit, OnChanges {
  @Input() user: User | null = null;
  @Input() isMobile = false;
  @Output() submit = new EventEmitter<UserRequest>();
  userForm: FormGroup<UserForm>
  roles = Object.values(Role)

  error: string | null = null
  // errorMatcher = new CrossFieldErrorMatcher();
  constructor() {
    this.userForm = new FormGroup<UserForm>({
      firstName: new FormControl('', { nonNullable: true, validators: Validators.required }),
      lastName: new FormControl('', { nonNullable: true, validators: Validators.required }),
      username: new FormControl('', { nonNullable: true, validators: Validators.required }),
      password: new FormControl(null),
      confirmPassword: new FormControl(null),
      role: new FormControl('', { nonNullable: true, validators: Validators.required }),
      email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
      phoneNumber: new FormControl(null),
    }, { validators: confirmPasswordValidator })
  }

  ngOnInit() {
    this.patchFormValue()
  }


  ngOnChanges(changes: SimpleChanges) {
    this.patchFormValue()
  }

  patchFormValue(){
    if (this.user) {
      this.userForm.controls.firstName.patchValue(this.user.firstName);
      this.userForm.controls.lastName.patchValue(this.user.lastName);
      this.userForm.controls.username.patchValue(this.user.username);
      this.userForm.controls.role.patchValue(this.user.role);
      this.userForm.controls.email.patchValue(this.user.email);
      this.userForm.controls.phoneNumber.patchValue(this.user.phoneNumber);
      this.userForm.controls.password.setValidators(Validators.required)
      this.userForm.controls.confirmPassword.setValidators(Validators.required)
    }
  }
  onSubmit() {
    if (this.userForm.valid) {
      this.error = null
      this.submit.emit(this.userForm.getRawValue())
    } else {
      this.error = 'required_fields_error'
    }
  }
}
