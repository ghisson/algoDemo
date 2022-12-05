package com.example.demo.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ValutazioneDTO;
import com.example.demo.Model.Utente;
import com.example.demo.Model.Valutazione;
import com.example.demo.Repository.UtenteRepository;
import com.example.demo.Repository.ValutazioneRepository;
import com.example.demo.Util.HashCreator;

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
		v.setIdTX(idTX);
		String valCript=serviceJWT.create(valutazioneDTO.getValutazione()+"");
		String noteCript=serviceJWT.create(valutazioneDTO.getNote());
		v.setValutazione(valCript);
		v.setNote(noteCript);
		valutazioneRepository.save(v);
		return v;
	}

}
