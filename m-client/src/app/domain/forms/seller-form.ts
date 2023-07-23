import { Form, FormControl } from "@angular/forms";

export interface SellerForm {
  name: FormControl<string>
  socialSecurityNumber: FormControl<string>
  address: FormControl<string | null>
  phoneNumber: FormControl<string | null>
  email: FormControl<string>
  additionalInfo: FormControl<string | null>
}
