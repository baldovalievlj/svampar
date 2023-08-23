import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { JwtHelperService } from "@auth0/angular-jwt";
import { LoginRequest } from "../domain/requests/login-request";
import {
  Observable,
  of,
  ReplaySubject,
  switchMap,
  tap
} from "rxjs";

import { Authentication } from "../domain/authentication";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private authenticationSubject = new ReplaySubject<Authentication | null>(1);
  private userDataFetched = false;

  constructor(private http: HttpClient, private jwtHelper: JwtHelperService) {
    this.fetchUserData()
  }

  getAuthentication(): Observable<Authentication | null> {
    if (!this.userDataFetched) {
      return this.fetchUserData().pipe(
        switchMap(() => this.authenticationSubject.asObservable())
      );
    } else {
      return this.authenticationSubject.asObservable();
    }
  }

  login(request: LoginRequest): Observable<any> {
    return this.http.post<any>('/api/login', request).pipe(
      tap(result => {
        this.setToken(result);
        this.fetchUserData()
      })
    )
  }

  setToken(authResult: { token: any; }) {
    console.log("Setting token:", authResult.token)
    localStorage.setItem('auth_token', authResult.token)
  }

  logout() {
    this.clearToken()
  }

  isAuthenticated() {
    console.log("getting token in isAuthenticated")
    const token = this.getToken()
    try {
      if (token && !this.jwtHelper.isTokenExpired(token)) {
        return true
      } else {
        console.log("Error in aisAuthenticated for token: ",token)
        console.log("jwtHelper return:", this.jwtHelper.isTokenExpired(token))
        this.logout()
        return false
      }
    } catch (e) {
      console.log("Catched error isAuthenticated: ",e)
      this.logout()
      return false
    }

  }

  fetchUserData(): Observable<Authentication | null> {
    if (this.isAuthenticated()) {
      return this.http.get<Authentication>('/api/authentication').pipe(
        tap(
          userData => {
            this.authenticationSubject.next(userData)
            this.userDataFetched = true;
          },
          error => {
            console.error('Error fetching user data failed to authenticate: ', error);
            this.logout();
          }
        )
      );
    } else {
      console.error('Error fetching user data user is not authenticated');
      return of(null);
    }
  }

  getToken(): string | null {
    console.log("Getting token")
    const token = localStorage.getItem('auth_token');
    return token;
  }

  clearToken(): void {
    localStorage.removeItem('auth_token');
  }
}
