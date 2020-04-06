package io.github.victoriholc.hoteljavaapi.repository.guest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.victoriholc.hoteljavaapi.model.guest.Guest;

/**
 * Extends the JPA CRUD methods.
 *  
 * @author Victor Freitas
 * @since 03/04/2020
 */
public interface GuestRepository extends JpaRepository<Guest, Long>{
	
	List<Guest> findByNome(String nome);
	
	List<Guest> findByDocumento(String documento);
	
	List<Guest> findByTelefone(String telefone);

	@Query(value = "select NEW Guest(g.id, g.nome, g.documento, g.telefone, g.valorGasto) from Guest g inner join Checkin c on g.id = c.id and"
			+ " data_saida is not null")
	List<Guest> findInHotel();
	
	@Query(value = "select NEW Guest(g.id, g.nome, g.documento, g.telefone, g.valorGasto) from Guest g inner join Checkin c on g.id = c.id and"
			+ " data_saida is null")
	List<Guest> findOutHotel();
}
