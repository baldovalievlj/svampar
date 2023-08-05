import { Component, OnInit } from '@angular/core';
import { DeviceDetectorService } from "ngx-device-detector";
import { TranslateService } from "@ngx-translate/core";
import { AuthenticationService } from "../../security/authentication.service";
import { UserService } from "../../services/user.service";
import { Router } from "@angular/router";
import { Authentication } from "../../domain/authentication";
import { MatDialog } from "@angular/material/dialog";
import {
  ChangePasswordDialogComponent
} from "../../components/change-password-dialog/change-password-dialog.component";
import { NgToastService } from "ng-angular-popup";

@Component({
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPage implements OnInit {
  isMobile = false;

  user: Authentication | null = null

  constructor(private deviceService: DeviceDetectorService,
              private translate: TranslateService,
              private authService: AuthenticationService,
              private userService: UserService,
              private router: Router,
              private dialog: MatDialog,
              private toast: NgToastService,) {
  }

  ngOnInit(): void {
    this.isMobile = this.deviceService.isMobile()
    this.translate.getDefaultLang()
    this.authService.getAuthentication().subscribe((res) => this.user = res)
  }

  onLogout() {
    this.authService.logout()
    this.router.navigateByUrl("/login")
  }

  onChangePassword() {
    const dialogRef = this.dialog.open(ChangePasswordDialogComponent, { data: { isCurrentPasswordRequired: true } })
    // TODO ADD THE ENDPOINT FOR CHANGING LOGGED USERS PASSWORD
    dialogRef.afterClosed().subscribe((result) => {
      if (result && this.user) {
        this.userService.updateLoggedInUserPassword(result).subscribe(
          (_) => {
            this.showSuccess(`${this.translate.instant("password")} ${this.translate.instant("updated_successfully")}`)
          }, error => {
            this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
          }
        )
      }
    })
  }

  showSuccess(message: string) {
    this.toast.success({ detail: this.translate.instant("success"), summary: message, duration: 3000, position: "br" })
  }

  showWarning(message: string) {
    this.toast.error({ detail: this.translate.instant("error"), summary: message, duration: 3000, position: "br" })
  }
}
