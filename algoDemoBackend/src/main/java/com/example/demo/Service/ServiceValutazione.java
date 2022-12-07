package com.example.demo.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.demo.DTO.ValutazioneDTO;
import com.example.demo.Model.Utente;
import com.example.demo.Model.Valutazione;
import com.example.demo.Repository.UtenteRepository;
import com.example.demo.Repository.ValutazioneRepository;
import com.example.demo.Util.HashCreator;

import jakarta.transaction.Transactional;

@Service
public class ServiceValutazione {
	
	@Autowired
	private ServiceTransaction serviceTransaction;
	
	@Autowired
	private ServiceJWT serviceJWT;
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	@Autowired
	private ValutazioneRepository valutazioneRepository;
	
	
	 
	public Valutazione addValutazione(ValutazioneDTO valutazioneDTO,long idUtente) throws NoSuchAlgorithmException {
		Valutazione v=new Valutazione();
		Optional<Utente> utente=utenteRepository.findById(idUtente);
		
		if(!utente.isPresent()) {
			return null;
		}
		v.setUtente(utente.get());
		valutazioneRepository.save(v);
		String hashPulito=v.getIdValutazione()+""+valutazioneDTO.getValutazione()+valutazioneDTO.getNote();
		String hash=HashCreator.createSHAHash(hashPulito);
		String idTX=serviceTransaction.sendTransaction(hash);
		if(idTX.equals("")) {
			valutazioneRepository.delete(v);
			return null;
		}
		v.setIdTX(idTX);
		String valCript=serviceJWT.create(valutazioneDTO.getValutazione()+"");
		String noteCript=serviceJWT.create(valutazioneDTO.getNote());
		v.setValutazione(valCript);
		v.setNote(noteCript);
		valutazioneRepository.save(v);
		return v;
	}
	
	public int checkValutazione(long idValutazione) throws Exception {
		
		Optional<Valutazione> v=valutazioneRepository.findById(idValutazione);
		if(!v.isPresent()) {
			return 0;
		}
		Valutazione valutazione=v.get();
		String val="";
		String note="";
		try {
			val=serviceJWT.verify(valutazione.getValutazione());
			note=serviceJWT.verify(valutazione.getNote());
		}catch(SignatureVerificationException exception) {
			return 3;
		}
		String noteHash=serviceTransaction.getNote(valutazione.getIdTX());
		if(noteHash==null) {
			return 1;
		}
		String hashPulito=valutazione.getIdValutazione()+val+note;
		String hash=HashCreator.createSHAHash(hashPulito);
		if(hash.equals(noteHash)){
			return 2;
		}
		return 3;
		
		
	}
	
	public List<Valutazione> getAllValutazioniByIdUtente(long idUtente){
		Optional<Utente> ut=utenteRepository.findById(idUtente);
		if(ut.isPresent()) {
			List<Valutazione> valutazioni=valutazioneRepository.findByUtente(ut.get());
			
			String note;
			String valutazione;
			for(Valutazione val:valutazioni) {
				try {
				note=serviceJWT.verify(val.getNote());
				valutazione=serviceJWT.verify(val.getValutazione());
				val.setNote(note);
				val.setValutazione(valutazione);
				}catch(SignatureVerificationException exception) {
					val.setNote("Dati compromessi");
					val.setValutazione("Dati compromessi");
				}
				
			}
			return valutazioni;
		}
		return null;
		
	}

}
