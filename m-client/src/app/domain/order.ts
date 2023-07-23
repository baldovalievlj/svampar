import { User } from "./user";
import { OrderItem } from "./order-item";
import { Seller } from "./seller";

export interface Order {
  id: number
  user: User
  seller: Seller
  dateCreated: Date
  details: String
  items: OrderItem[]
  totalPrice: number
  totalAmount: number
}
