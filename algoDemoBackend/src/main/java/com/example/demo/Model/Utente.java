package com.example.demo.Model;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
@Entity
public class Utente {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_utente")
	private long idUtente;
	private String name;
	private String surname;
	@Column(name="birth_date")
	private Date birthDate;
	@Column(name="cod_fiscale")
	private String codFiscale;
	private String email;
	private String password;
	
	public Utente() {}
	
	public Utente(long idUtente, String name, String surname, Date birthDate, String codFiscale,String email,String password) {
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
		return "Utente [idUtente=" + idUtente + ", name=" + name + ", surname=" + surname + ", birthDate=" + birthDate
				+ ", codFiscale=" + codFiscale + ", email=" + email + ", password=" + password + "]";
	}
	
	@OneToMany( mappedBy = "utente" )
    @JsonIgnoreProperties("utente")
	
	private List<Valutazione> valutazioni;

	public List<Valutazione> getValutazioni() {
		return valutazioni;
	}

	public void setValutazioni(List<Valutazione> valutazioni) {
		this.valutazioni = valutazioni;
	}
		
	
	
	
}
