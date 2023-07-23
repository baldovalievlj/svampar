import { Component, OnInit } from '@angular/core';
import { Seller } from "../../domain/seller";
import { ActivatedRoute } from "@angular/router";
import { SellerService } from "../../services/seller.service";
import { DeviceDetectorService } from "ngx-device-detector";
import { catchError, of, switchMap } from "rxjs";
import { Location } from '@angular/common';
import { SellerRequest } from "../../domain/requests/seller-request";
import { NgToastService } from "ng-angular-popup";
import { TranslateService } from "@ngx-translate/core";

@Component({
  templateUrl: './seller-create-edit.page.html',
  styleUrls: ['./seller-create-edit.page.css']
})
export class SellerCreateEditPage implements OnInit {
  sellerId: number | null = null;

  seller: Seller | null = null;

  isMobile = false;

  constructor(private route: ActivatedRoute,
              private location: Location,
              private sellerService: SellerService,
              private deviceService: DeviceDetectorService,
              private toast: NgToastService,
              private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.isMobile = this.deviceService.isMobile();
    this.route.params.pipe(
      switchMap((params) => {
        this.sellerId = +params['id'];
        if (this.sellerId) {
          return this.sellerService.getSeller(this.sellerId).pipe(
            catchError((error) => {
              this.showWarning(error.error ? this.translate.instant(error.error) : `${this.translate.instant("error_loading")} ${this.translate.instant('seller')}`);
              this.location.back();
              return of(null);
            })
          );
        } else {
          return of(null);
        }
      })
    ).subscribe((seller) => {
      this.seller = seller;
    });
  }

  onSubmit(request: SellerRequest) {
    if (this.seller) {
      this.sellerService.updateSeller(this.seller.id, request).subscribe(
        (_) => {
          this.showSuccess(`${this.translate.instant("seller")} ${this.translate.instant("updated_successfully")}`)
          this.location.back();
        }, error => {
          this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
        }
      );
    } else {
      this.sellerService.createSeller(request).subscribe(
        (_) => {
          this.showSuccess(`${this.translate.instant("seller")} ${this.translate.instant("created_successfully")}`)
          this.location.back();
        }, error => {
          this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
        }
      );
    }
  }

  showSuccess(message: string) {
    this.toast.success({ detail: this.translate.instant("success"), summary: message, duration: 3000, position: "br" })
  }

  showWarning(message: string) {
    this.toast.error({ detail: this.translate.instant("error"), summary: message, duration: 3000, position: "br" })
  }
}
