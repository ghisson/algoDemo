import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UtenteService } from '../service/utente.service';

@Component({
  selector: 'app-ca-login',
  templateUrl: './ca-login.component.html',
  styleUrls: ['./ca-login.component.css']
})
export class CaLoginComponent {

  dati: FormGroup;
  idUtente:any
  errore: boolean


  constructor(private fb: FormBuilder, private router: Router,private utenteService:UtenteService) {

    this.dati = this.fb.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required]]
    })

    this.errore = false;
    this.idUtente = this.utenteService.getId();
    console.log(this.idUtente)
    if(this.idUtente!=null && this.idUtente!=0){
      this.router.navigate(['/home']);
    }
  }


  invio(): void {

    this.utenteService.login(this.dati.get("email")?.value, this.dati.get("password")?.value).subscribe(
      (response: any) => {
        console.log(response)
        this.utenteService.setId(response.idUtente);
        this.utenteService.setLoggato();
        this.utenteService.setNome(response.name);
        this.utenteService.isUserLoggedIn.next(true);
        this.router.navigate(['/home']);
        this.errore = false;

      },
      (error: any) => {
        this.errore = true;
        this.utenteService.isUserLoggedIn.next(false);
      }
    )
    //sessionStorage.setItem("login", "true");
    //this.router.navigate(['/home'])
  }
}



