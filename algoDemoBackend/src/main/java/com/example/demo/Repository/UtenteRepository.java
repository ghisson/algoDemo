package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Model.Utente;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
	Optional<Utente> findByEmail(String email);
	Optional<Utente> findByCodFiscale(String codice_fiscale);
}
