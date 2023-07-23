import { Type } from "./type";

export interface OrderItem {
  id: number
  item: string
  type: Type
  amount: number
  price: number
}
