import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { BehaviorSubject, map, Observable, of, startWith, Subscription } from "rxjs";
import { Type } from "../../domain/type";
import { OrderRequest } from "../../domain/requests/order-request";
import { Seller } from "../../domain/seller";

@Component({
  selector: 'order-create-edit',
  templateUrl: './order-create-edit.component.html',
  styleUrls: ['./order-create-edit.component.css']
})
export class OrderCreateEditComponent implements OnInit, OnDestroy {

  @Input() isMobile: boolean = false;
  @Input() types$: BehaviorSubject<Type[]>;
  @Input() sellers$: BehaviorSubject<Seller[]>;
  @Output() submit = new EventEmitter<OrderRequest>;
  @Output() createType = new EventEmitter;
  @Output() createSeller = new EventEmitter;
  types: Type[] = [];
  sellers: Seller[] = []
  orderForm: FormGroup;
  private subscriptionTypes: Subscription = new Subscription();
  private subscriptionSellers: Subscription = new Subscription();


  filteredSellers$: Observable<Seller[]>;
  constructor(private fb: FormBuilder) {
    this.types$ = new BehaviorSubject<Type[]>([]);
    this.sellers$ = new BehaviorSubject<Seller[]>([]);
    this.filteredSellers$ = new Observable<Seller[]>();
    this.orderForm = this.fb.group({
      seller: ['', Validators.required],
      details: [''],
      date: [new Date()],
      items: this.fb.array([this.createItem()])
    });
  }

  ngOnInit() {
    this.subscriptionTypes = this.types$.subscribe((types) => {
      this.types = types;
    });

    this.subscriptionSellers = this.sellers$.subscribe((sellers) => {
      this.sellers = sellers;
    });

    this.filteredSellers$ = this.orderForm.get('seller')?.valueChanges
      .pipe(
        startWith(''),
        map(value => typeof value === 'string' ? value : value?.name),
        map(name => name ? this._filter(name) : this.sellers.slice())
      ) ?? of([]);
  }

  displayFn(seller: Seller): string {
    return seller && seller.name ? seller.name : '';
  }

  private _filter(name: string): Seller[] {
    const filterValue = name.toLowerCase();

    return this.sellers.filter(option => option.name.toLowerCase().includes(filterValue));
  }
  ngOnDestroy() {
    if (this.subscriptionTypes) {
      this.subscriptionTypes.unsubscribe();
    }
    if (this.subscriptionSellers) {
      this.subscriptionSellers.unsubscribe();
    }
  }

  get items(): FormArray {
    return this.orderForm.get('items') as FormArray;
  }

  get isMultipleItems(): boolean {
    return this.items.length > 1
  }

  createItem(): FormGroup {
    return this.fb.group({
      type: ['', Validators.required],
      comment: [''],
      amount: ['', [Validators.required, Validators.min(1)]],
      price: ['', [Validators.required, Validators.min(0)]]
    });
  }

  addItem(): void {
    this.items.push(this.createItem());
  }

  removeItem(index: number): void {
    this.items.removeAt(index);
  }

  onSubmit(): void {
    const formValue = this.orderForm.getRawValue();
    formValue.date = formValue.date.toISOString();
    this.submit.emit(formValue);
  }
}
