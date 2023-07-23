export interface Seller {
  id: number
  name: string
  socialSecurityNumber: string
  address: string | null
  phoneNumber: string | null
  email: string
  additionalInfo: string | null
}
