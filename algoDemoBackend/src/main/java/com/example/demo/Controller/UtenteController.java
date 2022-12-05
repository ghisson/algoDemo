package com.example.demo.Controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.DTO.ValutazioneDTO;
import com.example.demo.Model.Errore;
import com.example.demo.Model.Utente;
import com.example.demo.Model.UtenteLogin;
import com.example.demo.Model.Valutazione;
import com.example.demo.Service.ServiceJWT;
import com.example.demo.Service.ServiceTransaction;
import com.example.demo.Service.ServiceUtente;
import com.example.demo.Service.ServiceValutazione;

@Controller
@RequestMapping(path="/utente")
@CrossOrigin(origins = "*")
public class UtenteController {
	
	@Autowired
	private ServiceJWT serviceJWT;
	
	@Autowired
	private ServiceTransaction serviceTransaction;
	
	@Autowired 
	private ServiceUtente serviceUtente;
	
	@Autowired
	private ServiceValutazione serviceValutazione;
	
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
	
	@PostMapping("/createValutazione/{idUtente}")
	public ResponseEntity<Object> postBody(@RequestBody ValutazioneDTO valutazioneDTO,@PathVariable long idUtente) throws NoSuchAlgorithmException {
		Valutazione valutazione=serviceValutazione.addValutazione(valutazioneDTO, idUtente);
		return new ResponseEntity<Object>(valutazione, HttpStatus.OK);
	}
	
	
	@GetMapping("/checkValutazione/{idValutazione}")
	public ResponseEntity<Object> postBody(@PathVariable long idValutazione) throws Exception {
		//0 valutazione su db non trovata
		//1 idtx su blockchain non trovata
		//2 tutto ok
		//3 check falsato
		int result=serviceValutazione.checkValutazione(idValutazione);
		if(result==0) {
			return new ResponseEntity<Object>("idValutazione non trovata", HttpStatus.NOT_FOUND);
		}else if(result==1) {
			return new ResponseEntity<Object>("idtx non trovato in blockchain", HttpStatus.NOT_FOUND);
		}else if(result==2) {
			return new ResponseEntity<Object>("ok", HttpStatus.OK);
		}else if(result==3) {
			return new ResponseEntity<Object>("dato falsato", HttpStatus.BAD_REQUEST);
		}
			
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	
	@GetMapping("/getAllValutazioni/{idUtente}")
	public ResponseEntity<Object> getAllTransactions(@PathVariable long idUtente) throws Exception {
		
		List<Valutazione> valutazioni=serviceValutazione.getAllValutazioniByIdUtente(idUtente);
		if(valutazioni==null) {
			return new ResponseEntity<Object>(valutazioni, HttpStatus.BAD_REQUEST);
		}
			
		return new ResponseEntity<Object>(valutazioni, HttpStatus.OK);
	}
	 
}
