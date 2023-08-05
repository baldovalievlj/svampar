import { Type } from "./type";
export interface OrderItem {
  id: number
  comment: string
  type: Type
  amount: string
  price: string
}
