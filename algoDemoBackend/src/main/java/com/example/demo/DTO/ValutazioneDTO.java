package com.example.demo.DTO;

public class ValutazioneDTO {

	private int valutazione;
	private String note;
	
	public ValutazioneDTO(int valutazione, String note) {
		super();
		this.valutazione = valutazione;
		this.note = note;
	}
	public int getValutazione() {
		return valutazione;
	}
	public void setValutazione(int valutazione) {
		this.valutazione = valutazione;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	
	
}
