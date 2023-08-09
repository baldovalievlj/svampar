import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Order } from "../domain/order";
import { OrderRequest } from "../domain/requests/order-request";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private http: HttpClient) {
  }

  getOrders() {
    return this.http.get<Order[]>('/api/order')
  }

  getOrdersPaged(offset: number, limit: number, filters: any) {
    let params = new HttpParams()
      .set('offset', offset.toString())
      .set('limit', limit.toString());

    if (filters.userId) {
      params = params.set('userId', filters.userId);
    }
    if (filters.startDate) {
      params = params.set('startDate', filters.startDate);
    }
    if (filters.endDate) {
      params = params.set('endDate', filters.endDate);
    }

    return this.http.get<{ orders: Order[], totalCount: number }>("/api/order/paged", { params });
  }

  createOrder(request: OrderRequest) {
    return this.http.post('/api/order', request)
  }

  exportOrder(id: number, offset: number) {
    return this.http.post(`/api/order/${id}/export`, offset,{ responseType: 'blob' });
  }

  emailOrder(id: number, offset: number) {
    return this.http.post(`/api/order/${id}/email`, offset);
  }
}
