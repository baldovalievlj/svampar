import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { PasswordForm } from "../../domain/forms/password-form";
import { PasswordValidators } from "../../services/validators/password-validators";

@Component({
  selector: 'change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.css']
})
export class ChangePasswordDialogComponent {
  changePasswordForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { isCurrentPasswordRequired: boolean }) {

    this.changePasswordForm = new FormGroup<PasswordForm>({
        password: new FormControl('', { nonNullable: true, validators: [Validators.required, PasswordValidators.passwordLength] }),
        confirmPassword: new FormControl('', {
          nonNullable: true,
          validators: [Validators.required]
        }),
      }, { validators: PasswordValidators.passwordsMustMatch }
    )
  }

  changePassword(): void {
    if (this.changePasswordForm.valid) {
      const form = this.changePasswordForm.value;
      this.dialogRef.close(form);
    }
  }
}
