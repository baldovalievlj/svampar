import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPage } from "./pages/login/login.page";
import { MainPage } from "./pages/main/main-page.component";
import { AuthenticationGuard } from "./security/authentication.guard";
import { UserListPage } from "./pages/user/user-list.page";
import { UserCreateEditPage } from "./pages/user-create-edit/user-create-edit.page";
import { OrderPage } from "./pages/order/order.page";
import { OrderCreateEditPage } from "./pages/order-create-edit/order-create-edit.page";
import { Role } from "./domain/role";
import { RoleGuard } from "./security/role.guard";
import { TypeListPage } from "./pages/type-list/type-list.page";
import { TypeCreateEditPage } from "./pages/type-create-edit/type-create-edit.page";
import { DashboardPage } from "./pages/dashboard/dashboard.page";
import { SellerListPage } from "./pages/seller-list/seller-list.page";
import { SellerCreateEditPage } from "./pages/seller-create-edit/seller-create-edit.page";
import { SettingsPage } from "./pages/settings/settings.page";

const routes: Routes = [
  {
    path: 'login', component: LoginPage
  },
  {
    path: '', component: MainPage, canActivate: [AuthenticationGuard], children: [
      {
        path: 'users', component: UserListPage, canActivate: [RoleGuard], data: { roles: [Role.ADMIN, Role.SUPERADMIN] }
      },
      {
        path: 'users/create',
        component: UserCreateEditPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN] }
      },
      {
        path: 'users/edit/:id',
        component: UserCreateEditPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN] }
      },
      {
        path: 'orders',
        component: OrderPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN, Role.DISTRIBUTOR] }
      },
      {
        path: 'orders/create',
        component: OrderCreateEditPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN, Role.DISTRIBUTOR] }
      },
      {
        path: 'types',
        component: TypeListPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN, Role.DISTRIBUTOR] }
      },
      {
        path: 'types/create',
        component: TypeCreateEditPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN, Role.DISTRIBUTOR] }
      },
      {
        path: 'types/edit/:id',
        component: TypeCreateEditPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN, Role.DISTRIBUTOR] }
      },
      {
        path: 'sellers',
        component: SellerListPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN, Role.DISTRIBUTOR] }
      },
      {
        path: 'sellers/create',
        component: SellerCreateEditPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN, Role.DISTRIBUTOR] }
      },
      {
        path: 'sellers/edit/:id',
        component: SellerCreateEditPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN, Role.DISTRIBUTOR] }
      },
      {
        path: 'settings',
        component: SettingsPage,
        canActivate: [RoleGuard],
        data: { roles: [Role.ADMIN, Role.SUPERADMIN] }
      },
      {
        path: '', component: DashboardPage,
      }
    ]
  },
  {
    path: '**', redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

