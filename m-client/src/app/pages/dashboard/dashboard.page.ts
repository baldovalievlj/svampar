import { Component } from '@angular/core';
import { Order } from "../../domain/order";
import { ChartDataset, ChartOptions, ChartType, LabelItem } from "chart.js";
import { OrderService } from "../../services/order.service";
import * as moment from 'moment';

@Component({
  selector: 'dashboard',
  templateUrl: './dashboard.page.html',
  styleUrls: ['./dashboard.page.css']
})
export class DashboardPage {
  orders: Order[] = [];
  constructor(private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.orderService.getOrders().subscribe((orders: Order[]) => {
      this.orders = orders
    });
  }

}
