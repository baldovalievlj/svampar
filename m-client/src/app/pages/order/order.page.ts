import { Component, OnInit } from '@angular/core';
import { OrderService } from "../../services/order.service";
import { Order } from "../../domain/order";
import { Page } from "../../domain/page";
import { ActivatedRoute, Router } from "@angular/router";
import { combineLatest, forkJoin, switchMap, tap } from "rxjs";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { UserService } from "../../services/user.service";
import { User } from "../../domain/user";
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from "@angular/material/core";
import { Authentication } from "../../domain/authentication";
import { AuthenticationService } from "../../security/authentication.service";
import { saveAs } from 'file-saver';
import { NgToastService } from "ng-angular-popup";
import { TranslateService } from "@ngx-translate/core";

@Component({
  templateUrl: './order.page.html',
  styleUrls: ['./order.page.css']
})
export class OrderPage implements OnInit {

  orders: Order[] = [];
  users: User[] = []
  user: Authentication | null = null;
  totalCount: number = 0
  paged: Page = { size: 10, index: 0 }
  filters: FormGroup;

  constructor(private orderService: OrderService,
              private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private authService: AuthenticationService,
              private toast: NgToastService,
              private translate: TranslateService) {
    this.filters = new FormGroup({
      startDate: new FormControl<Date | null>(null),
      endDate: new FormControl<Date | null>(null),
      user: new FormControl('')
    });
  }

  ngOnInit(): void {
    combineLatest([
      this.userService.getUsers(),
      this.route.queryParamMap
    ]).subscribe(([users, params]) => {
      this.users = users;
      this.paged.index = +(params?.get('pageIndex') ?? 0);
      this.paged.size = +(params?.get('pageSize') ?? 10);
      const userId = params?.get('userId') || '';
      const startDate = params?.get('startDate') ? new Date(params.get('startDate')!) : null;
      const endDate = params?.get('endDate') ? new Date(params.get('endDate')!) : null;
      this.filters.patchValue({
        startDate: startDate,
        endDate: endDate,
        user: +userId
      }, { emitEvent: false });

      this.fetchOrders().subscribe(response => {
        this.orders = response.orders;
        this.totalCount = response.totalCount;
      });
    });
    this.authService.getAuthentication().subscribe((res) => this.user = res)
    this.filters.valueChanges.subscribe(() => {
      this.onUpdateParams(this.paged);
    });
  }


  // onUpdateParams(page: Page) {
  //   this.router.navigate(['/orders'], {
  //     queryParams: {
  //       pageIndex: page.index,
  //       pageSize: page.size,
  //       userId: this.filters.value.user || null,
  //       startDate: this.filters.value.startDate?.toISOString().split('T')[0] || null,
  //       endDate: this.filters.value.endDate?.toISOString().split('T')[0] || null
  //     },
  //     queryParamsHandling: 'merge'
  //   });
  // }
  onUpdateParams(page: Page) {
    const queryParams: any = {
      pageIndex: page.index,
      pageSize: page.size,
    };

    if (this.filters.value.user) {
      queryParams.userId = this.filters.value.user;
    }

    if (this.filters.value.startDate) {
      queryParams.startDate = this.filters.value.startDate.toISOString().split('T')[0];
    }

    if (this.filters.value.endDate) {
      queryParams.endDate = this.filters.value.endDate.toISOString().split('T')[0];
    }

    this.router.navigate(['/orders'], {
      queryParams,
      queryParamsHandling: 'merge',
    });
  }

  clearFilters(): void {
    this.filters.reset();
    this.router.navigate(['/orders'], {
      queryParams: {
        pageIndex: this.paged.index,
        pageSize: this.paged.size,
      },
    });
  }

  fetchOrders() {
    const filters = {
      userId: this.filters.value.user,
      startDate: this.filters.value.startDate ? this.filters.value.startDate.toISOString().split('T')[0] : null,
      endDate: this.filters.value.endDate ? this.filters.value.endDate.toISOString().split('T')[0] : null
    };

    return this.orderService.getOrdersPaged(this.paged.index * this.paged.size, this.paged.size, filters);
  }

  onOrderExport(id: number) {
    const timeZoneOffsetMinutes = new Date().getTimezoneOffset();
    this.orderService.exportOrder(id, timeZoneOffsetMinutes).subscribe((file: Blob) =>
      saveAs(file, `Order-${id}.pdf`)
    )
  }

  onOrderEmail(id: number) {
    const timeZoneOffsetMinutes = new Date().getTimezoneOffset();
    this.orderService.emailOrder(id, timeZoneOffsetMinutes).subscribe((_) => {
        this.showSuccess(`${this.translate.instant("email")} ${this.translate.instant("sent_successfully")}`)
      }, error => {
        this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
      }
    );
  }

  showSuccess(message: string) {
    this.toast.success({ detail: this.translate.instant("success"), summary: message, duration: 3000, position: "br" })
  }

  showWarning(message: string) {
    this.toast.error({ detail: this.translate.instant("error"), summary: message, duration: 3000, position: "br" })
  }
}
