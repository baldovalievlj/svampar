import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot, UrlTree,
} from '@angular/router';
import { first, Observable, of, switchMap } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthenticationService } from './authentication.service';
import { AuthenticationGuard } from './authentication.guard';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthenticationService, private router: Router, private authGuard: AuthenticationGuard) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    const expectedRoles = route.data['roles'];
    return this.authGuard.canActivate(route, state).pipe(
      switchMap((isAuthenticated: boolean | UrlTree) => {
        if (isAuthenticated instanceof UrlTree) {
          return of(isAuthenticated);
        }
        return this.authService.getAuthentication().pipe(
          map(authentication => {
            if (expectedRoles.includes(authentication?.role)) {
              return true;
            } else {
              return this.router.parseUrl('/');
            }
          }),
          first()
        );
      })
    );
  }
}
