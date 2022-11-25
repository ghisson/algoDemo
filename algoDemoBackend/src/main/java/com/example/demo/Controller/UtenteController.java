package com.example.demo.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Model.Errore;
import com.example.demo.Model.Utente;
import com.example.demo.Model.UtenteLogin;
import com.example.demo.Service.ServiceJWT;
import com.example.demo.Service.ServiceUtente;

@Controller
@RequestMapping(path="/utente")
public class UtenteController {
	
	@Autowired
	private ServiceJWT serviceJWT;
	
	@Autowired 
	private ServiceUtente serviceUtente;	
	
	@CrossOrigin(origins = "*")
	@PostMapping("/login")
	public ResponseEntity<Object> postLogin(@RequestBody UtenteLogin user) {
		Errore errore=new Errore();
	    Utente utente = serviceUtente.checkLogin(user);
	    
	    if(utente!=null) {
	    	return new ResponseEntity<Object>(utente, HttpStatus.OK);
	    }
	    errore.setError("login NON avvenuto con successo");
	    return new ResponseEntity<Object>(errore, HttpStatus.BAD_REQUEST);
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping("/addUtente")
	public ResponseEntity<Object> postBody(@RequestBody Utente utente) {
		Errore errore=new Errore();
		String psw=utente.getPassword();
		psw=serviceJWT.create(psw);
		utente.setPassword(psw);
		Utente utn=serviceUtente.addUser(utente);
		if(utn!=null) {
			return new ResponseEntity<Object>(utn, HttpStatus.OK);
		}
		errore.setError("utente già esistente");
		return new ResponseEntity<Object>(errore, HttpStatus.BAD_REQUEST);
	}
	 
}
