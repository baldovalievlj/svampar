import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { Order } from "../../../domain/order";
import { ChartDataset, ChartOptions, ChartType } from "chart.js";

@Component({
  selector: 'orders-per-user-diagram',
  templateUrl: './orders-per-user-diagram.component.html',
  styleUrls: ['./orders-per-user-diagram.component.css']
})
export class OrdersPerUserDiagramComponent implements OnInit, OnChanges {
  @Input() orders: Order[] = []
  public barChartLabels: string[] = [];
  public barChartOptions: ChartOptions = {
    responsive: true,
  };
  public barChartType: ChartType = 'bar';
  public barChartLegend = true;
  public barChartPlugins = [];
  public barChartData: ChartDataset[] = [
    { data: [], label: 'Total Orders' },
    { data: [], label: 'Total Price' }
  ];

  ngOnInit(): void {
    const data = this.calculateData(this.orders);
    this.barChartLabels = data.labels;
    this.barChartData[0].data = data.totalOrders;
    this.barChartData[1].data = data.totalPrices;
  }
  ngOnChanges(): void {
    const data = this.calculateData(this.orders);
    this.barChartLabels = data.labels;
    this.barChartData[0].data = data.totalOrders;
    this.barChartData[1].data = data.totalPrices;
  }
  calculateData(orders: Order[]) {
    let labels = [];
    let totalOrders = [];
    let totalPrices = [];

    const groupedOrders = orders.reduce<{ [key: string]: { count: number, totalPrice: number } }>((acc, order) => {
      if (!acc[order.user.username]) {
        acc[order.user.username] = {
          count: 0,
          totalPrice: 0
        };
      }
      acc[order.user.username].count++;
      acc[order.user.username].totalPrice += order.totalPrice;
      return acc;
    }, {});

    for (const username in groupedOrders) {
      labels.push(username); // or user name
      totalOrders.push(groupedOrders[username].count);
      totalPrices.push(groupedOrders[username].totalPrice);
    }

    return {
      labels,
      totalOrders,
      totalPrices
    };
  }

}
