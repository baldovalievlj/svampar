import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { UserForm } from "../../domain/forms/user-form";
import { User } from "../../domain/user";
import { Role } from "../../domain/role";
import { UserRequest } from "../../domain/requests/user-request";
import { PasswordForm } from "../../domain/forms/password-form";
import { ConfirmValidParentMatcher, PasswordValidators } from "../../services/validators/password-validators";
import { ErrorStateMatcher } from "@angular/material/core";

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
  confirmValidParentMatcher: ErrorStateMatcher;
  hidePassword = true;
  hideConfirmPassword = true;

  constructor(private fb: FormBuilder) {
    this.confirmValidParentMatcher = new ConfirmValidParentMatcher()
    this.userForm = this.fb.group<UserForm>({
      firstName: new FormControl('', { nonNullable: true, validators: Validators.required }),
      lastName: new FormControl('', { nonNullable: true, validators: Validators.required }),
      username: new FormControl('', { nonNullable: true, validators: Validators.required }),
      role: new FormControl('', { nonNullable: true, validators: Validators.required }),
      email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
      phoneNumber: new FormControl(null),
    })
  }

  ngOnInit() {
    this.patchFormValue()
  }

  ngOnChanges(changes: SimpleChanges) {
    this.patchFormValue()
  }

  patchFormValue() {
    if (this.user) {
      this.userForm.controls.firstName.patchValue(this.user.firstName);
      this.userForm.controls.lastName.patchValue(this.user.lastName);
      this.userForm.controls.username.patchValue(this.user.username);
      this.userForm.controls.role.patchValue(this.user.role);
      this.userForm.controls.email.patchValue(this.user.email);
      this.userForm.controls.phoneNumber.patchValue(this.user.phoneNumber);
      if (this.userForm.contains('passwordGroup')) {
        this.userForm.removeControl('passwordGroup'); // remove passwordGroup for existing user
      }
    } else {
      this.userForm.addControl('passwordGroup', this.fb.group<PasswordForm>({
          password: new FormControl('', {
            nonNullable: true,
            validators: [Validators.required, PasswordValidators.passwordLength]
          }),
          confirmPassword: new FormControl('', {
            nonNullable: true,
            validators: [Validators.required]
          }),
        }, { validators: PasswordValidators.passwordsMatch })
      )
    }
  }

  onSubmit() {
    if (this.userForm.valid) {
      const formValue = this.userForm.getRawValue();
      const userRequest: UserRequest = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        username: formValue.username,
        role: formValue.role,
        email: formValue.email,
        phoneNumber: formValue.phoneNumber,
        password: formValue.passwordGroup ? formValue.passwordGroup.password : null,
        confirmPassword: formValue.passwordGroup ? formValue.passwordGroup.confirmPassword : null
      };
      this.submit.emit(userRequest)
    }
  }
}
