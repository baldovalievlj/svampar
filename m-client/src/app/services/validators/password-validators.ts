import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormGroupDirective, NgForm,
  ValidationErrors,
  ValidatorFn
} from "@angular/forms";
import { ErrorStateMatcher } from "@angular/material/core";

export class PasswordValidators {
  static passwordsMustMatch: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return password && confirmPassword && password === confirmPassword ? null : { passwordMismatch: true };
  }
//   export class CustomValidators {
//   /**
//    * Validates that child controls in the form group are equal
//    */
//   static childrenEqual: ValidatorFn = (formGroup: FormGroup) => {
//     const [firstControlName, ...otherControlNames] = Object.keys(formGroup.controls || {});
//     const isValid = otherControlNames.every(controlName => formGroup.get(controlName).value === formGroup.get(firstControlName).value);
//     return isValid ? null : { childrenNotEqual: true };
//   }
// }
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
export class ConfirmValidParentMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return (control?.parent?.invalid && control?.touched) || false;
  }
}
