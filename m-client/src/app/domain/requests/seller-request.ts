export interface SellerRequest{
  name: string
  socialSecurityNumber: string
  address: string | null
  phoneNumber: string | null
  email: string
  additionalInfo: string | null
}
