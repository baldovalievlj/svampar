import { Form, FormControl, FormGroup } from "@angular/forms";
import { PasswordForm } from "./password-form";

export interface UserForm {
  firstName: FormControl<string>
  lastName: FormControl<string>
  username: FormControl<string>
  passwordGroup?: FormGroup<PasswordForm>
  role: FormControl<string>
  email: FormControl<string>
  phoneNumber: FormControl<string | null>
}
