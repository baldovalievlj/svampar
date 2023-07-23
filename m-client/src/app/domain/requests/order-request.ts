
export interface OrderRequest {
  details: string | null
  seller: number
  items: {type: string, amount: string, price: string, comment: string}[]
}
