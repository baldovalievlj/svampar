import { Component, OnInit } from '@angular/core';
import { SellerService } from '../../services/seller.service';
import { Seller } from '../../domain/seller';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../../components/confirmation-dialog/confirmation-dialog.component';
import { Page } from "../../domain/page";
import { ActivatedRoute, Router } from "@angular/router";
import { switchMap } from "rxjs";

@Component({
  templateUrl: './seller-list.page.html',
  styleUrls: ['./seller-list.page.css'],
})
export class SellerListPage implements OnInit {
  sellers: Seller[] = [];
  totalCount: number = 0;
  paged: Page = { size: 10, index: 0 };
  selectedSellerId: number | null;

  constructor(private sellerService: SellerService, private dialog: MatDialog, private route: ActivatedRoute, private router: Router) {
    this.selectedSellerId = null;
  }

  ngOnInit(): void {
    this.route.queryParamMap.pipe(
      switchMap(params => {
        this.paged.index = +(params?.get('pageIndex') ?? 0);
        this.paged.size = +(params?.get('pageSize') ?? 10);

        return this.sellerService.getSellersPaged(this.paged.index * this.paged.size, this.paged.size);
      })
    ).subscribe(response => {
      this.sellers = response.sellers;
      this.totalCount = response.totalCount;
    });
  }

  onDelete(id: number) {
    this.selectedSellerId = id;
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, { data: { entity: 'seller' } });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && this.selectedSellerId) {
        this.sellerService.deleteSeller(this.selectedSellerId).subscribe(
          (_) => this.sellers = this.sellers.filter(it => it.id !== this.selectedSellerId)
        );
      }
    });
  }

  onPageChange(page: Page) {
    this.router.navigate(['/sellers'], {
      queryParams: {
        pageIndex: page.index,
        pageSize: page.size
      }
    });
  }
}
