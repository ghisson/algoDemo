import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CaHomeComponent } from './ca-home/ca-home.component';
import { CaLoginComponent } from './ca-login/ca-login.component';
import { CaRegistrazioneComponent } from './ca-registrazione/ca-registrazione.component';

const routes: Routes = [
  {path: 'login', component: CaLoginComponent},
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {path: 'home', component: CaHomeComponent},
  {path: 'signup', component: CaRegistrazioneComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
