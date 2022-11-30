package com.example.demo.Model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Valutazione {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_valutazione")
	private long idValutazione;
	private String valutazione;
	private String note;
	private String hash;
	
	public Valutazione() {}
	
	public Valutazione(String valutazione, String note, String hash) {
		super();
		this.valutazione = valutazione;
		this.note = note;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public long getIdValutazione() {
		return idValutazione;
	}

	public String getValutazione() {
		return valutazione;
	}

	public void setValutazione(String valutazione) {
		this.valutazione = valutazione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@ManyToOne
    @JoinColumn(name = "fk_id_utente", referencedColumnName = "id_utente")
    @JsonIgnoreProperties("valutazione")
	
	private Utente utente;

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	
	
	
}
