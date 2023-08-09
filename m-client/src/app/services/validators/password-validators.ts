import { AbstractControl, FormGroup, ValidationErrors, ValidatorFn } from "@angular/forms";

export class PasswordValidators {
  static passwordsMatch: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } | null => {
    const group = control as FormGroup;
    const password = group.get('password')?.value;
    const confirmPassword = group.controls['confirmPassword'].value;
    return password && confirmPassword && password === confirmPassword ? null : { passwordMismatch: true };
  };
  static passwordLength: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const password = control.value;
    return password && password.length >= 8 ? null : { passwordLength: true };
  }
}
