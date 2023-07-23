import { User } from "./user";
import { OrderItem } from "./order-item";
import { Seller } from "./seller";
export interface Order {
  id: number
  user: User
  seller: Seller
  dateCreated: Date
  details: string | null
  items: OrderItem[]
  totalPrice: string
  totalAmount: string
}
