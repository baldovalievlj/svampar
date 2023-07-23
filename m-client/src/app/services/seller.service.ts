import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Seller } from "../domain/seller";
import { Observable } from "rxjs";
import { SellerRequest } from "../domain/requests/seller-request";

@Injectable({
  providedIn: 'root'
})
export class SellerService {

  constructor(private http: HttpClient) {
  }

  getSellers(): Observable<Seller[]> {
    return this.http.get<Seller[]>('/api/seller');
  }

  getSellersPaged(offset: number, limit: number): Observable<{ sellers: Seller[], totalCount: number }> {
    return this.http.get<{ sellers: Seller[], totalCount: number }>(`/api/seller/paged?offset=${offset}&limit=${limit}`);
  }

  getSeller(id: number): Observable<Seller> {
    return this.http.get<Seller>(`/api/seller/${id}`);
  }

  createSeller(request: SellerRequest): Observable<Seller> {
    return this.http.post<Seller>('/api/seller', request);
  }

  updateSeller(id: number, request: SellerRequest) {
    return this.http.put(`/api/seller/${id}`, request);
  }

  deleteSeller(id: number) {
    return this.http.delete(`/api/seller/${id}`);
  }
}
