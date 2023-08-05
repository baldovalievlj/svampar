import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { UserService } from "../../services/user.service";
import { catchError, of, switchMap } from "rxjs";
import { User } from "../../domain/user";
import { Location } from '@angular/common';
import { DeviceDetectorService } from "ngx-device-detector";
import { UserRequest } from "../../domain/requests/user-request";
import { NgToastService } from 'ng-angular-popup';
import { TranslateService } from "@ngx-translate/core";

@Component({
  templateUrl: './user-create-edit.page.html',
  styleUrls: ['./user-create-edit.page.css']
})
export class UserCreateEditPage implements OnInit {
  userId: number | null = null
  user: User | null = null;
  isMobile = false

  constructor(private route: ActivatedRoute,
              private location: Location,
              private userService: UserService,
              private deviceService: DeviceDetectorService,
              private toast: NgToastService,
              private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.isMobile = this.deviceService.isMobile()
    this.route.params.pipe(
      switchMap((params) => {
        this.userId = +params['id']
        if (this.userId) {
          return this.userService.getUser(this.userId).pipe(
            catchError((error) => {
              this.showWarning(error.error ? this.translate.instant(error.error) : `${this.translate.instant("error_loading")} ${this.translate.instant('users')}`);
              this.location.back()
              return of(null);
            })
          );
        } else {
          return of(null);
        }
      })
    ).subscribe((user) => {
      this.user = user;
    });
  }

  onSubmit(request: UserRequest) {
    if (this.user) {
      this.userService.updateUser(this.user.id, request).subscribe(
        (_) => {
          this.showSuccess(`${this.translate.instant("user")} ${this.translate.instant("created_successfully")}`)
          this.location.back()
        }, error => {
          this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
        }
      )
    } else {
      this.userService.createUser(request).subscribe(
        (_) => {
          this.showSuccess(`${this.translate.instant("user")} ${this.translate.instant("created_successfully")}`)
          this.location.back()
        }, error => {
          this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
        }
      )
    }
  }

  showSuccess(message: string) {
    this.toast.success({ detail: this.translate.instant("success"), summary: message, duration: 3000, position: "br" })
  }

  showWarning(message: string) {
    this.toast.error({ detail: this.translate.instant("error"), summary: message, duration: 3000, position: "br" })
  }
}
