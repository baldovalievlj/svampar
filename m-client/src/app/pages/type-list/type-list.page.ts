import { Component, OnInit } from '@angular/core';
import { TypeService } from '../../services/type.service';
import { Type } from '../../domain/type';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../../components/confirmation-dialog/confirmation-dialog.component';
import { Page } from "../../domain/page";
import { ActivatedRoute, Router } from "@angular/router";
import { switchMap } from "rxjs";

@Component({
  templateUrl: './type-list.page.html',
  styleUrls: ['./type-list.page.css'],
})
export class TypeListPage implements OnInit {
  types: Type[] = [];
  totalCount: number = 0
  paged: Page = { size: 10, index: 0 }
  selectedTypeId: number | null;

  constructor(private typeService: TypeService, private dialog: MatDialog, private route: ActivatedRoute, private router: Router) {
    this.selectedTypeId = null;
  }

  ngOnInit(): void {
    this.route.queryParamMap.pipe(
      switchMap(params => {
        this.paged.index = +(params?.get('pageIndex') ?? 0);
        this.paged.size = +(params?.get('pageSize') ?? 10);

        return this.typeService.getTypesPaged(this.paged.index * this.paged.size, this.paged.size);
      })
    ).subscribe(response => {
      this.types = response.types;
      this.totalCount = response.totalCount;
    });
  }

  onDelete(id: number) {
    this.selectedTypeId = id;
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, { data: { entity: 'type' } });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && this.selectedTypeId) {
        this.typeService.deleteType(this.selectedTypeId).subscribe(
          (_) => this.types = this.types.filter(it => it.id !== this.selectedTypeId)
        );
      }
    });
  }
  onPageChange(page: Page) {
    this.router.navigate(['/types'], {
      queryParams: {
        pageIndex: page.index,
        pageSize: page.size
      }
    });
  }
}
