package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Model.Utente;
import com.example.demo.Model.Valutazione;

public interface ValutazioneRepository  extends JpaRepository<Valutazione, Long> {
	List<Valutazione> findByUtente(Utente utente);
}
