import { Type } from "./type";
export interface OrderItem {
  id: number
  item: string
  type: Type
  amount: string
  price: string
}
