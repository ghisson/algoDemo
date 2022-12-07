import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


const baseUrl:string="http://localhost:8080/utente"

@Injectable({
  providedIn: 'root'
})
export class ValutazioniService {

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
      "Access-Control-Allow-Origin": "*",
    } ), 
  };

  constructor(private http: HttpClient) { }
  

  getAllValutazioneByIdUtente(idUtente:any):Observable<any> {   
    return this.http.get<any>(baseUrl+'/getAllValutazioni/'+idUtente);
  }


  checkValutazione(idValutazione:any):Observable<any> {   
    return this.http.get<any>(baseUrl+'/checkValutazione/'+idValutazione);
  }

}
