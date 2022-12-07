import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CaLoginComponent } from './ca-login/ca-login.component';
import { UtenteService } from './service/utente.service';
import { CaHomeComponent } from './ca-home/ca-home.component';
import { CaNavbarComponent } from './ca-navbar/ca-navbar.component';
import { CaRegistrazioneComponent } from './ca-registrazione/ca-registrazione.component';

@NgModule({
  declarations: [
    AppComponent,
    CaLoginComponent,
    CaHomeComponent,
    CaNavbarComponent,
    CaRegistrazioneComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [UtenteService],
  bootstrap: [AppComponent]
})
export class AppModule { }
