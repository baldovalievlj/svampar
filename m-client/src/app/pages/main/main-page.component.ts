import { Component, OnInit } from '@angular/core';
import { DeviceDetectorService } from "ngx-device-detector";
import { TranslateService } from "@ngx-translate/core";
import { AuthenticationService } from "../../security/authentication.service";
import { UserService } from "../../services/user.service";
import { Router } from "@angular/router";
import { Authentication } from "../../domain/authentication";

@Component({
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPage implements OnInit {
  isMobile = false;

  user: Authentication | null = null

  constructor(private deviceService: DeviceDetectorService,
              private translationService: TranslateService,
              private authService: AuthenticationService,
              private userService: UserService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.isMobile = this.deviceService.isMobile()
    this.translationService.getDefaultLang()
    this.authService.getAuthentication().subscribe((res) => this.user = res)
  }

  onLogout(){
    this.authService.logout()
    this.router.navigateByUrl("/login")
  }

}
