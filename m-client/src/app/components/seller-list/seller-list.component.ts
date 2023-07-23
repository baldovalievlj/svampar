import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Seller } from "../../domain/seller";
import { Page } from "../../domain/page";
import { PageEvent } from "@angular/material/paginator";

@Component({
  selector: 'seller-list',
  templateUrl: './seller-list.component.html',
  styleUrls: ['./seller-list.component.css'],
})
export class SellerListComponent {
  @Input() sellers: Seller[] = [];
  @Input() totalCount: number = 0;
  @Input() page: Page = { size: 10, index: 0 }
  @Output() delete = new EventEmitter<number>();
  @Output() pageChange = new EventEmitter<Page>;

  columnsToDisplay = ['id', 'name', 'socialSecurity', 'address', 'phone','email','additionalInfo', 'actions'];
  pageSizeOptions = [5, 10, 20];

  onPageChange(event: PageEvent) {
    this.pageChange.emit({ size: event.pageSize, index: event.pageIndex })
  }
}
