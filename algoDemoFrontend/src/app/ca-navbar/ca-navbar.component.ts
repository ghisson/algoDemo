import { Component } from '@angular/core';
import { UtenteService } from '../service/utente.service';

@Component({
  selector: 'app-ca-navbar',
  templateUrl: './ca-navbar.component.html',
  styleUrls: ['./ca-navbar.component.css']
})
export class CaNavbarComponent {

  loggato:any

  constructor(private utenteService:UtenteService){
    this.utenteService.isUserLoggedIn.subscribe( value => {
      this.loggato=value+"";
      console.log(this.loggato)
    });
    this.loggato = utenteService.getLoggato()
    

    
  }

}
