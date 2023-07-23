import { Component, OnInit } from '@angular/core';
import { Type } from "../../domain/type";
import { ActivatedRoute } from "@angular/router";
import { TypeService } from "../../services/type.service";
import { DeviceDetectorService } from "ngx-device-detector";
import { catchError, of, switchMap } from "rxjs";
import { Location } from '@angular/common';
import { TypeRequest } from "../../domain/requests/type-request";
import { NgToastService } from "ng-angular-popup";
import { TranslateService } from "@ngx-translate/core";

@Component({
  templateUrl: './type-create-edit.page.html',
  styleUrls: ['./type-create-edit.page.css']
})
export class TypeCreateEditPage implements OnInit {
  typeId: number | null = null;

  type: Type | null = null;

  isMobile = false;

  constructor(private route: ActivatedRoute,
              private location: Location,
              private typeService: TypeService,
              private deviceService: DeviceDetectorService,
              private toast: NgToastService,
              private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.isMobile = this.deviceService.isMobile();
    this.route.params.pipe(
      switchMap((params) => {
        this.typeId = +params['id'];
        if (this.typeId) {
          return this.typeService.getType(this.typeId).pipe(
            catchError((error) => {
              this.showWarning(error.error ? this.translate.instant(error.error) : `${this.translate.instant("error_loading")} ${this.translate.instant('types_mushroom')}`);
              this.location.back();
              return of(null);
            })
          );
        } else {
          return of(null);
        }
      })
    ).subscribe((type) => {
      this.type = type;
    });
  }

  onSubmit(request: TypeRequest) {
    if (this.type) {
      this.typeService.updateType(this.type.id, request).subscribe(
        (_) => {
          this.showSuccess(`${this.translate.instant("type")} ${this.translate.instant("created_successfully")}`)
          this.location.back();
        }, error => {
          this.showWarning(this.translate.instant(error.error ?? 'unknown_error'))
        }
      );
    } else {
      this.typeService.createType(request).subscribe(
        (_) => {
          this.showSuccess(`${this.translate.instant("type")} ${this.translate.instant("created_successfully")}`)
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
