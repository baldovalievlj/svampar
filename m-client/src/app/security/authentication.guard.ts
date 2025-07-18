import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthenticationService } from "./authentication.service";

@Injectable({
  providedIn: 'root',
})
export class AuthenticationGuard implements CanActivate {
  constructor(private authService: AuthenticationService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    return this.checkAuthentication();
  }

  private checkAuthentication(): Observable<boolean | UrlTree> {
    const isAuthenticated = this.authService.isAuthenticated();
    if (isAuthenticated) {
      return of(true);
    }
    return of(this.router.parseUrl('/login'));
  }
}
