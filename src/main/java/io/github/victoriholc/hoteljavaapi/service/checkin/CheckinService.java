package io.github.victoriholc.hoteljavaapi.service.checkin;

import java.util.List;
import java.util.Optional;

import io.github.victoriholc.hoteljavaapi.model.checkin.Checkin;


/**
 * Provides methods for manipulating Check in objects.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
public interface CheckinService {
	
	Checkin save(Checkin transaction);
	
	void deleteById(Long id);
	
	Optional<Checkin> findById(Long id);
	
	List<Checkin> findAll();
}
