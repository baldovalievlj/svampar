import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Type } from "../../domain/type";
import { Page } from "../../domain/page";
import { PageEvent } from "@angular/material/paginator";

@Component({
  selector: 'type-list',
  templateUrl: './type-list.component.html',
  styleUrls: ['./type-list.component.css'],
})
export class TypeListComponent {
  @Input() types: Type[] = [];
  @Input() totalCount: number = 0;
  @Input() page: Page = { size: 10, index: 0 }
  @Output() delete = new EventEmitter<number>();
  @Output() pageChange = new EventEmitter<Page>;

  columnsToDisplay = ['id', 'name', 'description', 'actions'];
  pageSizeOptions = [5, 10, 20];

  onPageChange(event: PageEvent) {
    this.pageChange.emit({ size: event.pageSize, index: event.pageIndex })
  }
}
