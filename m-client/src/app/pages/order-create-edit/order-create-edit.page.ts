import { Component, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { DeviceDetectorService } from "ngx-device-detector";
import { OrderService } from "../../services/order.service";
import { TypeService } from "../../services/type.service";
import { BehaviorSubject } from "rxjs";
import { Type } from "../../domain/type";
import { ActivatedRoute } from "@angular/router";
import { Location } from "@angular/common";
import { OrderRequest } from "../../domain/requests/order-request";
import { MatDialog } from "@angular/material/dialog";
import { CreateTypeDialogComponent } from "../../components/create-type-dialog/create-type-dialog.component";
import { NgToastService } from "ng-angular-popup";
import { TranslateService } from "@ngx-translate/core";
import { SellerService } from "../../services/seller.service";
import { Seller } from "../../domain/seller";
import { CreateSellerDialogComponent } from "../../components/create-seller-dialog/create-seller-dialog.component";
import { SellerRequest } from "../../domain/requests/seller-request";

@Component({
  templateUrl: './order-create-edit.page.html',
  styleUrls: ['./order-create-edit.page.css']
})
export class OrderCreateEditPage implements OnInit {
  isMobile = false
  types$ = new BehaviorSubject<Type[]>([]);
  sellers$ = new BehaviorSubject<Seller[]>([]);

  constructor(private route: ActivatedRoute,
              private location: Location,
              private orderService: OrderService,
              private deviceService: DeviceDetectorService,
              private typeService: TypeService,
              private dialog: MatDialog,
              private toast: NgToastService,
              private translate: TranslateService,
              private sellerService: SellerService) {
  }

  ngOnInit(): void {
    this.isMobile = this.deviceService.isMobile()
    this.typeService.getTypes().subscribe(result =>
      this.types$.next(result)
    )
    this.sellerService.getSellers().subscribe(result =>
      this.sellers$.next(result))
  }

  onSubmit(request: OrderRequest) {
    this.orderService.createOrder(request).subscribe(
      (_) => {
        this.showSuccess(`${this.translate.instant("order")} ${this.translate.instant("created_successfully")}`)
        this.location.back();
      }, error => {
        this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
      }
    )
  }

  onCreateType() {
    const dialogRef = this.dialog.open(CreateTypeDialogComponent, {
      width: '400px',
      data: {} // You can pass any initial data if needed.
    });

    dialogRef.afterClosed().subscribe((newType: Type) => {
      if (newType) {
        this.typeService.createType(newType).subscribe({
            next: (type) => {
              this.showSuccess(`${this.translate.instant("type")} ${this.translate.instant("created_successfully")}`)
              this.types$.next([...this.types$.value, type])
            }
          }
        )
      }
    });
  }

  onCreateSeller() {
    const dialogRef = this.dialog.open(CreateSellerDialogComponent, {
      width: '400px',
      data: {} // You can pass any initial data if needed.
    });

    dialogRef.afterClosed().subscribe((newSeller: SellerRequest) => {
      if (newSeller) {
        this.sellerService.createSeller(newSeller).subscribe({
            next: (seller) => {
              this.showSuccess(`${this.translate.instant("seller")} ${this.translate.instant("created_successfully")}`)
              this.sellers$.next([...this.sellers$.value, seller])
            },
            error: error => {
              this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
            }
          }
        )
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
