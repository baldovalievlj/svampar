import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent, HttpErrorResponse,
} from '@angular/common/http';
import { catchError, Observable, of, throwError } from 'rxjs';
import { Router } from "@angular/router";
import { NgToastService } from "ng-angular-popup";
import { TranslateService } from "@ngx-translate/core";
import { AuthenticationService } from "./authentication.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router,
              private toast: NgToastService,
              private translate: TranslateService,
              private authService: AuthenticationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken()
    console.log("getting token in intercept for request:", req)
    if (token && req.url.includes("/api")) {
      req = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`),
      });
    }
    console.log("getting token in intercept for request with headers:", req)
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        console.log("Error status in interceptor:", error.status)
        console.log("Error message in interceptor:", error.message)
        if (error.status === 401) {
          this.authService.clearToken()
          this.router.navigate(['/login']);
        }
        else if (error.status === 404) {
          this.toast.error({
            detail: this.translate.instant("error"),
            summary: this.translate.instant("not_found"),
            duration: 3000,
            position: "br"
          })
        }
        else {
          this.toast.error({
            detail: this.translate.instant("error"),
            summary: error.message,
            duration: 3000,
            position: "br"
          })
        }
        return of()
      })
    );
  }
}
