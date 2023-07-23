import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginPage } from './pages/login/login.page';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './components/login/login.component';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {FlexModule} from "@angular/flex-layout";
import {MatIconModule} from "@angular/material/icon";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from "@angular/common/http";
import { MainPage } from './pages/main/main-page.component';
import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { MatListModule } from "@angular/material/list";
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { AuthInterceptor } from "./security/authentication.interceptor";
import { UserListPage } from './pages/user/user-list.page';
import { RouterModule } from "@angular/router";
import { UserListComponent } from './components/user-list/user-list.component';
import { MatTableModule } from "@angular/material/table";
import { ChangePasswordDialogComponent } from './components/change-password-dialog/change-password-dialog.component';
import { ConfirmationDialogComponent } from './components/confirmation-dialog/confirmation-dialog.component';
import { MatDialogModule } from "@angular/material/dialog";
import { JwtModule } from "@auth0/angular-jwt";
import { UserCreateEditPage } from './pages/user-create-edit/user-create-edit.page';
import { UserCreateEditComponent } from './components/user-create-edit/user-create-edit.component';
import { MatNativeDateModule, MatOptionModule } from "@angular/material/core";
import { MatSelectModule } from "@angular/material/select";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { OrderPage } from './pages/order/order.page';
import { OrderListComponent } from './components/order-list/order-list.component';
import { OrderCreateEditComponent } from './components/order-create-edit/order-create-edit.component';
import { OrderCreateEditPage } from "./pages/order-create-edit/order-create-edit.page";
import { MatTooltipModule } from "@angular/material/tooltip";
import { TypeListComponent } from './components/type-list/type-list.component';
import { TypeListPage } from "./pages/type-list/type-list.page";
import { TypeCreateEditComponent } from './components/type-create-edit/type-create-edit.component';
import { TypeCreateEditPage } from "./pages/type-create-edit/type-create-edit.page";
import { CreateTypeDialogComponent } from './components/create-type-dialog/create-type-dialog.component';
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { DashboardPage } from './pages/dashboard/dashboard.page';
import { OrderWeeklyDiagramComponent } from './components/diagrams/order-weekly-diagram/order-weekly-diagram.component';
import { NgChartsModule } from "ng2-charts";
import { NgToastModule } from 'ng-angular-popup';
import { SellerListPage } from './pages/seller-list/seller-list.page';
import { SellerListComponent } from './components/seller-list/seller-list.component';
import { SellerCreateEditPage } from './pages/seller-create-edit/seller-create-edit.page';
import { SellerCreateEditComponent } from './components/seller-create-edit/seller-create-edit.component';
import { CreateSellerDialogComponent } from './components/create-seller-dialog/create-seller-dialog.component'
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { OrdersPerUserDiagramComponent } from './components/diagrams/orders-per-user-diagram/orders-per-user-diagram.component';
import { SettingsPage } from './pages/settings/settings.page';
import { AngularEditorModule } from '@kolkov/angular-editor';
@NgModule({
  declarations: [
    AppComponent,
    LoginPage,
    LoginComponent,
    MainPage,
    NavBarComponent,
    UserListPage,
    UserListComponent,
    ChangePasswordDialogComponent,
    ConfirmationDialogComponent,
    UserCreateEditPage,
    UserCreateEditComponent,
    OrderPage,
    OrderListComponent,
    OrderCreateEditComponent,
    OrderCreateEditPage,
    TypeListComponent,
    TypeListPage,
    TypeCreateEditComponent,
    TypeCreateEditPage,
    CreateTypeDialogComponent,
    DashboardPage,
    OrderWeeklyDiagramComponent,
    SellerListPage,
    SellerListComponent,
    SellerCreateEditPage,
    SellerCreateEditComponent,
    CreateSellerDialogComponent,
    OrdersPerUserDiagramComponent,
    SettingsPage
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        RouterModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatInputModule,
        MatButtonModule,
        NgbModule,
        FlexModule,
        MatIconModule,
        ReactiveFormsModule,
        HttpClientModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: httpTranslateLoader,
                deps: [HttpClient]
            }
        }),
        JwtModule.forRoot({
            config: {
                tokenGetter: tokenGetter,
                allowedDomains: ['http://127.0.0.1:8080/'],
                disallowedRoutes: [],
            },
        }),
        MatListModule,
        MatSidenavModule,
        MatToolbarModule,
        MatProgressSpinnerModule,
        MatTableModule,
        MatDialogModule,
        MatOptionModule,
        MatSelectModule,
        MatGridListModule,
        MatSnackBarModule,
        BrowserAnimationsModule,
        MatTooltipModule,
        MatPaginatorModule,
        FormsModule,
        MatDatepickerModule,
        MatNativeDateModule,
        NgChartsModule,
        NgToastModule,
        MatAutocompleteModule,
        AngularEditorModule
    ],
  providers: [ {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
export function httpTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
export function tokenGetter() {
  return localStorage.getItem('auth_token');
}
