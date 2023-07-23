export interface OrderRequest {
  details: string
  seller: number
  items: {type: number, amount: number, price: number, comment: string}[]
}
