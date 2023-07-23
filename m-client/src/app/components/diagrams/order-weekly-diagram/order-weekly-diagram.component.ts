import { Component, Input } from '@angular/core';
import { ChartDataset, ChartOptions, ChartType  } from 'chart.js';
@Component({
  selector: 'app-order-weekly-diagram',
  templateUrl: './order-weekly-diagram.component.html',
  styleUrls: ['./order-weekly-diagram.component.css']
})
export class OrderWeeklyDiagramComponent {
  @Input() chartLabels: string[] = [];
  @Input() chartData: ChartDataset[] = [];
  @Input() chartOptions: ChartOptions = {};

  public chartType: ChartType = 'line';
  public chartLegend = true;
  public chartPlugins = [];
}
