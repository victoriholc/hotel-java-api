package io.github.victoriholc.hoteljavaapi.service.guest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.victoriholc.hoteljavaapi.model.guest.Guest;
import io.github.victoriholc.hoteljavaapi.repository.guest.GuestRepository;

/**
 * Implements the guest's service methods
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
@Service
public class GuestServiceImpl implements GuestService {
	
	@Autowired
	private GuestRepository repository;

	@Override
	public Guest save(Guest guest) {
		return repository.save(guest);
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Optional<Guest> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<Guest> findByNome(String nome) {
		return repository.findByNome(nome);
	}

	@Override
	public List<Guest> findByDocumento(String documento) {
		return repository.findByDocumento(documento);
	}

	@Override
	public List<Guest> findByTelefone(String telefone) {
		return repository.findByTelefone(telefone);
	}

	@Override
	public List<Guest> findAll() {
		return repository.findAll();
	}

	@Override
	public List<Guest> findInHotel() {
		return repository.findInHotel();
	}
	
	@Override
	public List<Guest> findOutHotel() {
		return repository.findOutHotel();
	}

}
