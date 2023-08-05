import { FormControl } from "@angular/forms";

export interface PasswordForm {
  currentPassword?: FormControl<string>
  password: FormControl<string>
  confirmPassword: FormControl<string>
}
