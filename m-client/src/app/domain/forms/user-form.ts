import { Form, FormControl } from "@angular/forms";

export interface UserForm {
  firstName: FormControl<string>
  lastName: FormControl<string>
  username: FormControl<string>
  password: FormControl<string | null>
  confirmPassword: FormControl<string | null>
  role: FormControl<string>
  email: FormControl<string>
  phoneNumber: FormControl<string | null>
}
