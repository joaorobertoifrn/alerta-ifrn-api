package br.edu.ifrn.alerta.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrn.alerta.model.Alerta;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Integer> {
	
}
