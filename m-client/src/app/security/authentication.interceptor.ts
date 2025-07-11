import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent, HttpErrorResponse,
} from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
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
    if (token && req.url.includes("/api")) {
      req = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`),
      });
    }
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error("Error while intercepting request:", error.message)
        if (error.status === 401) {
          this.toast.error({
            detail: this.translate.instant("error"),
            summary: error.error,
            duration: 3000,
            position: "br"
          })
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
