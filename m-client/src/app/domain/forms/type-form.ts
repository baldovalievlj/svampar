import { FormControl } from "@angular/forms";

export interface TypeForm {
  name: FormControl<string>
  description: FormControl<string | null>
}
