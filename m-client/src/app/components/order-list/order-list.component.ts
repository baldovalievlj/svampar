import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { animate, state, style, transition, trigger } from "@angular/animations";
import { MatTableDataSource } from "@angular/material/table";
import { Order } from "../../domain/order";
import { Page } from "../../domain/page";
import { PageEvent } from "@angular/material/paginator";

@Component({
  selector: 'order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class OrderListComponent implements OnInit, OnChanges {
  @Input() orders: Order[] = []
  @Input() totalCount: number = 0;
  @Input() page: Page = { size: 10, index: 0 }
  @Output() pageChange = new EventEmitter<Page>;
  @Output() export = new EventEmitter<number>;
  @Output() email = new EventEmitter<number>;

  ordersDataSource: MatTableDataSource<any> = new MatTableDataSource();
  ordersColumns: string[] = ['id', 'username', 'seller', 'details', 'amount', 'price', 'dateCreated', 'actions'];
  pageSizeOptions = [5, 10, 20]

  ngOnInit(): void {
    const orders = this.orders.map((element) => ({
      ...element,
      isExpanded: false
    }))

    this.ordersDataSource = new MatTableDataSource<Order>(orders)
  }

  ngOnChanges(changes: SimpleChanges) {
    const orders = this.orders.map((element) => ({
      ...element,
      isExpanded: false
    }))
    this.ordersDataSource = new MatTableDataSource<Order>(orders)
  }

  onPageChange(event: PageEvent) {
    this.pageChange.emit({ size: event.pageSize, index: event.pageIndex })
  }

  getDate(dateCreated: string) {
    return new Date(dateCreated)
  }
}
