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
	private String idTX;
	
	public Valutazione() {}
	
	public Valutazione(String valutazione, String note, String idTX) {
		super();
		this.valutazione = valutazione;
		this.note = note;
		this.idTX=idTX;
	}

	public String getIdTX() {
		return idTX;
	}

	public void setIdTX(String idTX) {
		this.idTX = idTX;
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
