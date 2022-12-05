import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CaLoginComponent } from './ca-login/ca-login.component';

const routes: Routes = [
  {path: 'login', component: CaLoginComponent},
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
