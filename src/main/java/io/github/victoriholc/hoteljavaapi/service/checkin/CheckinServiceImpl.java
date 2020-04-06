package io.github.victoriholc.hoteljavaapi.service.checkin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.victoriholc.hoteljavaapi.model.checkin.Checkin;
import io.github.victoriholc.hoteljavaapi.repository.checkin.CheckinRepository;


/**
 * Implements the transaction's service methods
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
@Service
public class CheckinServiceImpl implements CheckinService {
	
	@Autowired
	private CheckinRepository repository;

	@Override
	public Checkin save(Checkin transaction) {
		return repository.save(transaction);
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Optional<Checkin> findById(Long id) {
		return repository.findById(id);
	}


	@Override
	public List<Checkin> findAll() {
		return repository.findAll();
	}

}
