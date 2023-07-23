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
    localStorage.setItem('auth_token', authResult.token)
  }

  logout() {
    localStorage.removeItem("auth_token");
  }

  isAuthenticated() {
    const token = localStorage.getItem('auth_token')
    try {
      if (token && !this.jwtHelper.isTokenExpired(token)) {
        return true
      } else {
        this.logout()
        return false
      }
    } catch (e) {
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
            this.logout();
            console.error('Failed to authenticate', error);
          }
        )
      );
    } else {
      return of(null);
    }
  }
}
