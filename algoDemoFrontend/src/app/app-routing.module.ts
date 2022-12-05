import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CaHomeComponent } from './ca-home/ca-home.component';
import { CaLoginComponent } from './ca-login/ca-login.component';

const routes: Routes = [
  {path: 'login', component: CaLoginComponent},
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {path: 'home', component: CaHomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
