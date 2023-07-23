import { Component, OnInit } from '@angular/core';
import { UserService } from "../../services/user.service";
import { User } from "../../domain/user";
import { MatDialog } from "@angular/material/dialog";
import { ConfirmationDialogComponent } from "../../components/confirmation-dialog/confirmation-dialog.component";
import { ChangePasswordDialogComponent } from "../../components/change-password-dialog/change-password-dialog.component";
import { Page } from "../../domain/page";
import { ActivatedRoute, Router } from "@angular/router";
import { switchMap } from "rxjs";
import { NgToastService } from "ng-angular-popup";
import { TranslateService } from "@ngx-translate/core";
import { AuthenticationService } from "../../security/authentication.service";
import { Authentication } from "../../domain/authentication";

@Component({
  templateUrl: './user-list.page.html',
  styleUrls: ['./user-list.page.css']
})
export class UserListPage implements OnInit {

  user: Authentication | null = null;
  users: User[] = []
  totalCount: number = 0
  paged: Page = { size: 10, index: 0 }
  selectedUserId: number | null;

  constructor(private userService: UserService,
              private dialog: MatDialog,
              private route: ActivatedRoute,
              private router: Router,
              private toast: NgToastService,
              private translate: TranslateService,
              private authService: AuthenticationService) {
    this.selectedUserId = null
  }

  ngOnInit(): void {
    this.route.queryParamMap.pipe(
      switchMap(params => {
        this.paged.index = +(params?.get('pageIndex') ?? 0);
        this.paged.size = +(params?.get('pageSize') ?? 10);

        return this.userService.getUsersPaged(this.paged.index * this.paged.size, this.paged.size);
      })
    ).subscribe(response => {
      this.users = response.users;
      this.totalCount = response.totalCount;
    });

    this.authService.getAuthentication().subscribe((res) => this.user = res)
  }

  onChangePassword(id: number) {
    this.selectedUserId = id;
    const dialogRef = this.dialog.open(ChangePasswordDialogComponent, { data: { isCurrentPasswordRequired: false } })

    dialogRef.afterClosed().subscribe((result) => {
      if (result && this.selectedUserId) {
        this.userService.updateUserPassword(this.selectedUserId, result).subscribe(
          (_) => {
            this.showSuccess(`${this.translate.instant("password")} ${this.translate.instant("updated_successfully")}`)
          }, error => {
            this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
          }
        )
      }
    })
  }

  onDelete(id: number) {
    this.selectedUserId = id;
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, { data: { entity: 'user' } });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && this.selectedUserId) {
        this.userService.deleteUser(this.selectedUserId).subscribe((_) => {
          this.showSuccess(`${this.translate.instant("user")} ${this.translate.instant("deleted_successfully")}`)
          this.users = this.users.filter(it => it.id != this.selectedUserId)
          this.selectedUserId = null
        }, error => {
          this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
        })
      }
    })
  }

  onPageChange(page: Page) {
    this.router.navigate(['/users'], {
      queryParams: {
        pageIndex: page.index,
        pageSize: page.size
      }
    });
  }

  showSuccess(message: string) {
    this.toast.success({ detail: this.translate.instant("success"), summary: message, duration: 3000, position: "br" })
  }

  showWarning(message: string) {
    this.toast.error({ detail: this.translate.instant("error"), summary: message, duration: 3000, position: "br" })
  }
}
