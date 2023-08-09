import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { User } from "../domain/user";
import { Observable } from "rxjs";
import { UserRequest } from "../domain/requests/user-request";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>("/api/user")
  }

  getUsersPaged(offset: number, limit: number): Observable<{ users: User[], totalCount: number }> {
    return this.http.get<{ users: User[], totalCount: number }>(`/api/user/paged?offset=${offset}&limit=${limit}`);
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(`api/user/${id}`)
  }

  createUser(request: UserRequest) {
    return this.http.post('/api/user', request)
  }

  updateUser(id: number, request: UserRequest) {
    return this.http.put(`/api/user/${id}`, request)
  }

  updateUserPassword(id: number, request: { password: string, confirmPassword: string }) {
    return this.http.put(`/api/user/${id}/password`, request)
  }

  updateLoggedInUserPassword( request: { currentPassword: string, password: string, confirmPassword: string }) {
    return this.http.put(`/api/authentication/change_password`, request)
  }

  deleteUser(id: number) {
    return this.http.delete(`/api/user/${id}`)
  }
}
