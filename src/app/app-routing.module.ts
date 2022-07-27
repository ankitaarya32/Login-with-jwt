import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { UserdetailComponent } from './components/userdetail/userdetail.component';
import { AuthgardGuard } from './services/authgard.guard';

const routes: Routes = [
  { 
    path: '' , component : HomeComponent,pathMatch : 'full'
},
{ 
  path: 'login' , component : LoginComponent,pathMatch : 'full'
},
{ 
  path: 'dashboard' , component : DashboardComponent,pathMatch : 'full',canActivate : [AuthgardGuard]
},
{ 
  path: 'users' , component : UserdetailComponent,pathMatch : 'full',canActivate : [AuthgardGuard]
},
{ 
  path: 'register' , component : RegisterComponent,pathMatch : 'full'
}
];
@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { } 
