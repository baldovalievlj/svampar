import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Type } from "../domain/type";
import { Observable, of } from "rxjs";
import { UserRequest } from "../domain/requests/user-request";
import { TypeRequest } from "../domain/requests/type-request";
import { User } from "../domain/user";

@Injectable({
  providedIn: 'root'
})
export class TypeService {

  constructor(private http: HttpClient) {
  }

  getTypes(): Observable<Type[]> {
    return this.http.get<Type[]>('/api/type')
  }

  getTypesPaged(offset: number, limit: number): Observable<{ types: Type[], totalCount: number }> {
    return this.http.get<{ types: Type[], totalCount: number }>(`/api/type/paged?offset=${offset}&limit=${limit}`);
  }

  getType(id: number): Observable<Type> {
    return this.http.get<Type>(`/api/type/${id}`)
  }

  createType(request: TypeRequest): Observable<Type> {
    return this.http.post<Type>('/api/type', request)
  }

  updateType(id: number, request: TypeRequest) {
    return this.http.put(`/api/type/${id}`, request)
  }

  deleteType(id: number) {
    return this.http.delete(`/api/type/${id}`)
  }
}
