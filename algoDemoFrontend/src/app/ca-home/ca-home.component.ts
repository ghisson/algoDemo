import { ThisReceiver } from '@angular/compiler';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Valutazione } from '../model/valutazione';
import { UtenteService } from '../service/utente.service';
import { ValutazioniService } from '../service/valutazioni.service';

@Component({
  selector: 'app-ca-home',
  templateUrl: './ca-home.component.html',
  styleUrls: ['./ca-home.component.css']
})
export class CaHomeComponent {
  idUtente:any;
  dati: FormGroup;
  loading:boolean;
  error:boolean;
 
  valutazioni:Valutazione[];


  constructor(private router: Router,private utenteService: UtenteService, private fb: FormBuilder, private valutazioniService:ValutazioniService) {
    this.idUtente = this.utenteService.getId();
    this.valutazioni=[];
    this.error=false;
    this.loading=false;
    console.log(this.idUtente);
    

    this.dati = this.fb.group({
      note: ['', [Validators.required]],
      valutazione: ['', [Validators.required,Validators.pattern("^[0-5]*$"), Validators.maxLength(1)]]
    });

    if(this.idUtente==0 || this.idUtente==null){
      this.router.navigate(['/login']);
    }

    this.loadDati()
  }

  checkMutability(num:string,idTX:any):void{
    // console.log(num);
    // const box = document.getElementById(num);
    //   box?.classList.add('list-group-item-danger');
    this.valutazioniService.checkValutazione(idTX).subscribe(
      (response:any) => {
        const box = document.getElementById(num);
        box?.classList.add('list-group-item-success');

      },
      (error:any) => {
        const box = document.getElementById(num);
        box?.classList.add('list-group-item-danger');
      }
    );
  }

  invio(){
    this.loading=true;
    this.valutazioniService.addValutazione(this.dati.value,this.idUtente).subscribe(
      async (response:any) => {
        console.log(response)
        this.error=false;
        this.loadDati()
        this.loading=false;
        await this.delay(1);
        const x = document.getElementById("closeModal");
        x?.click();
      },
      async (error:any) => {
        console.log(error)
        this.loading=false;
        this.error=true;
      }
    );
  }


  loadDati(){
    this.valutazioni=[];
    this.valutazioniService.getAllValutazioneByIdUtente(this.idUtente).subscribe(
      (response:any) => {
        console.log(response);
        for(let i=0;i<response.length;i++){
          this.valutazioni.push(new Valutazione(response[i]));
        }
        console.log(this.valutazioni);

      },
      (error:any) => {

      }
    );
  }


  delay(ms: number) {
    console.log("dormo")
    return new Promise( resolve => setTimeout(resolve, ms) );
  }



}
