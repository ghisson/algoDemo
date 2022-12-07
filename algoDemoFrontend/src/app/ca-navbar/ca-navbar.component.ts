import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UtenteService } from '../service/utente.service';

@Component({
  selector: 'app-ca-navbar',
  templateUrl: './ca-navbar.component.html',
  styleUrls: ['./ca-navbar.component.css']
})
export class CaNavbarComponent {

  loggato:any
  nomeUtente:any

  constructor(private utenteService:UtenteService, private router: Router){
    this.utenteService.isUserLoggedIn.subscribe( value => {
      this.loggato=value;
      console.log(this.loggato);
    });
    this.loggato = utenteService.getLoggato()
    this.nomeUtente=utenteService.getNome();
    console.log(this.loggato);
    

    
  }

  logout(){
    this.utenteService.setLogout();
    this.router.navigate(['/login']);
    console.log(this.nomeUtente);
  }

  login() {
    this.router.navigate(['/login']);
  }

}
