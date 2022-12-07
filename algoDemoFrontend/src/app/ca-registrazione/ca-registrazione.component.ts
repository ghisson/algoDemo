import { Component, ɵflushModuleScopingQueueAsMuchAsPossible } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UtenteService } from '../service/utente.service';

@Component({
  selector: 'app-ca-registrazione',
  templateUrl: './ca-registrazione.component.html',
  styleUrls: ['./ca-registrazione.component.css']
})
export class CaRegistrazioneComponent {

  dati: FormGroup;
  errore:boolean;
  idUtente:any;

  constructor(private fb: FormBuilder,private router: Router,private utenteService:UtenteService) {
    
    this.errore=false;

    this.idUtente = this.utenteService.getId();
    if(this.idUtente!=null && this.idUtente!=0){
      this.router.navigate(['/home']);
    }

    this.dati = this.fb.group({
      name: ['', [Validators.required]],
      surname: ['', [Validators.required]],
      birthDate: ['', [Validators.required]],
      codFiscale: ['', [Validators.required]],
      email: ['', [Validators.required]],
      password: ['', [Validators.required]]
    })
  }

  invio(): void {
    this.utenteService.signUp(this.dati.value).subscribe(
      (response: any) => {
        this.errore=false;
        this.router.navigate(['/login']);

      },
      (error: any) => {
        this.errore=true;
      }
    )
  }
}
