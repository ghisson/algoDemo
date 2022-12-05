package com.example.demo.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Model.Errore;
import com.example.demo.Model.Utente;
import com.example.demo.Model.UtenteLogin;
import com.example.demo.Repository.UtenteRepository;

@Service
public class ServiceUtente {
	
	@Autowired
	private ServiceJWT serviceJWT;
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	 public Utente checkLogin(UtenteLogin user) {
		 Optional<Utente> ut=utenteRepository.findByEmail(user.getEmail());
		 if (ut.isPresent()) {
		    	Utente utn = ut.get();
		    	String psw=serviceJWT.verify(utn.getPassword());
		    	if(psw.equals(user.getPassword())) {
		    		return utn;
		    	}
		 }
		 return null;
	 }
	 
	 public Utente addUser(Utente user) {
		 Optional<Utente> ut=utenteRepository.findByCodFiscale(user.getCodFiscale());
		  if(ut.isPresent()) {
			  return null;
		  }
		  ut=utenteRepository.findByEmail(user.getEmail());
		  if(ut.isPresent()) {
			  return null;
		  }
		  utenteRepository.save(user);
		  return user;
	 }
	 
	 
	 
}
