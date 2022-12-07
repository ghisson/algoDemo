import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';


const baseUrl:string="http://localhost:8080/utente"



@Injectable({
  providedIn: 'root'
})
export class UtenteService {

  public isUserLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);


  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json',
      "Access-Control-Allow-Origin": "*",
    } ), 
  };

  constructor(private http: HttpClient) { }


  login(email:string, password:string):Observable<any> {   
    return this.http.post<any>(baseUrl+'/login', {"email":email,"password":password},this.httpOptions);
  }

  setNome(nome:string):void{
    sessionStorage.setItem("nome", nome);
  }
  getNome(){
    return sessionStorage.getItem("nome");
  }


  setLoggato(){
    sessionStorage.setItem("login", "true");
  }

  setId(id:number){
      sessionStorage.setItem("id", id+"");
  }

  getId(){
      return sessionStorage.getItem("id");
  }

  setLogout(){
      sessionStorage.setItem("nome", "");
      sessionStorage.setItem("login", "false");
      sessionStorage.setItem("id", "0");
      this.isUserLoggedIn.next(false);
  }

  getLoggato():boolean{
      if(sessionStorage.getItem("login")=="true"){
          return true;
      }
      return false;
  }

  signUp(dati:any){
    return this.http.post<any>(baseUrl+'/addUtente', dati,this.httpOptions);
  }

}
