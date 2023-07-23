import { FormControl } from "@angular/forms";

export interface UserRequest {
  firstName: string
  lastName: string
  username: string
  password: string | null
  confirmPassword: string | null
  role: string
  email: string
  phoneNumber: string | null
}
