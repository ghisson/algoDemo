import { ThisReceiver } from '@angular/compiler';
import { Component } from '@angular/core';
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
 
  valutazioni:Valutazione[];


  constructor(private router: Router,private utenteService: UtenteService, private valutazioniService:ValutazioniService) {
    this.idUtente = this.utenteService.getId();
    this.valutazioni=[];
    console.log(this.idUtente);
    if(this.idUtente==0 || this.idUtente==null){
      this.router.navigate(['/login']);
    }

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

  becomered(num:string):void{
    console.log(num);
    const box = document.getElementById(num);
      box?.classList.add('list-group-item-danger');
  }




}
