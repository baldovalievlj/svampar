import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from "../../domain/user";
import { PageEvent } from "@angular/material/paginator";
import { Page } from "../../domain/page";
import { Authentication } from "../../domain/authentication";

@Component({
  selector: 'user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent {
  @Input() authentication: Authentication | null = null;
  @Input() users: User[] = [];
  @Input() totalCount: number = 0;
  @Input() page: Page = { size: 10, index: 0 }
  @Output() changePassword = new EventEmitter<number>;
  @Output() delete = new EventEmitter<number>;
  @Output() pageChange = new EventEmitter<Page>;

  columnsToDisplay = ['id', 'name', 'username','role', 'email', 'phoneNumber', 'actions'];
  pageSizeOptions = [5, 10, 20]

  onPageChange(event: PageEvent) {
    this.pageChange.emit({ size: event.pageSize, index: event.pageIndex })
  }
}
