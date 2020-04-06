package io.github.victoriholc.hoteljavaapi.service.guest;

import java.util.List;
import java.util.Optional;

import io.github.victoriholc.hoteljavaapi.model.guest.Guest;

/**
 * Provides methods for manipulating Statistics objects.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */

public interface GuestService {
	
	Guest save(Guest statistic);

	void deleteById(Long id);
	
	Optional<Guest> findById(Long id);
	
	List<Guest> findByNome(String nome);
	
	List<Guest> findByDocumento(String documento);
	
	List<Guest> findByTelefone(String telefone);
	
	List<Guest> findInHotel();
	
	List<Guest> findOutHotel();
	
	List<Guest> findAll();
}
