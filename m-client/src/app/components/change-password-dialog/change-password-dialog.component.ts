import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { PasswordForm } from "../../domain/forms/password-form";
import { PasswordValidators } from "../../services/validators/password-validators";
import { ConfirmValidParentMatcher } from "../../services/validators/confirm-valid-parent-matcher";

@Component({
  selector: 'change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.css']
})
export class ChangePasswordDialogComponent {
  changePasswordForm: FormGroup;
  confirmValidParentMatcher: ConfirmValidParentMatcher;
  hideCurrentPassword = true;
  hidePassword = true;
  hideConfirmPassword = true;
  constructor(public dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { isCurrentPasswordRequired: boolean }) {

    this.confirmValidParentMatcher = new ConfirmValidParentMatcher();
    this.changePasswordForm = new FormGroup<PasswordForm>({
        password: new FormControl('', {
          nonNullable: true,
          validators: [Validators.required, PasswordValidators.passwordLength]
        }),
        confirmPassword: new FormControl('', {
          nonNullable: true,
          validators: [Validators.required]
        }),
      }, { validators: PasswordValidators.passwordsMatch }
    )

    if (this.data.isCurrentPasswordRequired) {
      this.changePasswordForm.addControl('currentPassword', new FormControl('', {
          nonNullable: true,
          validators: [Validators.required, PasswordValidators.passwordLength]
        }
      ));
    }
  }

  changePassword(): void {
    if (this.changePasswordForm.valid) {
      const form = this.changePasswordForm.value;
      this.dialogRef.close(form);
    }
  }
}
