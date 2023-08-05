import { AbstractControl, FormGroup, ValidationErrors, ValidatorFn } from "@angular/forms";
import { confirmPasswordValidator } from "./confirm-password-validator";

export class PasswordValidators {
  static passwordsMustMatch: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return password && confirmPassword && password === confirmPassword ? null : { passwordMismatch: true };
  }

  static passwordLength: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const password = control.value;
    return password && password.length >= 8 ? null : { passwordLength: true };
  }
}
