package com.example.demo.Model;

import java.sql.Date;

public class Utente {
	private long idUtente;
	private String name;
	private String surname;
	private Date birthDate;
	private String codFiscale;
	private String email;
	private String password;
	
	public Utente() {}
	
	public Utente(long idUtente, String name, String surname, Date birthDate, String codFiscale,String email) {
		super();
		this.idUtente=idUtente;
		this.name=name;
		this.surname=surname;
		this.birthDate=birthDate;
		this.codFiscale=codFiscale;
		this.email=email;
		this.password=password;
	}

	public long getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(long idUtente) {
		this.idUtente = idUtente;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email=email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password=password;
	}
	
	@Override
	public String toString() {
		return "Utente [idUtente="+idUtente+", nome="+name+", cognome="+surname+", dataDiNascita="+birthDate+", codiceFiscale="+codFiscale+"]";
	}
}
