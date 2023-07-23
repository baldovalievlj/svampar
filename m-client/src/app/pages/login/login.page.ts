import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from "../../security/authentication.service";
import { LoginRequest } from "../../domain/requests/login-request";
import { Router } from "@angular/router";

@Component({
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.css']
})
export class LoginPage implements OnInit {
  loading = false
  errorMessage: string | null = null;

  constructor(private authService: AuthenticationService, private router: Router) {
  }

  ngOnInit() {
    if(this.authService.isAuthenticated()){
      this.router.navigate(['/'])
    }
  }

  onLogin(request: LoginRequest) {
    this.loading = true
    this.authService.login(request).subscribe(this.onSuccess, this.onError)
  }

  onSuccess = (response: any) => {
    this.loading = false;
    this.errorMessage = null;
    this.router.navigateByUrl("/")
    window.location.reload();
  }

  onError = (err: any) => {
    this.loading = false;
    this.errorMessage = err.error
  }
}
