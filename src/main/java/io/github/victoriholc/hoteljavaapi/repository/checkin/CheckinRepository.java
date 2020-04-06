package io.github.victoriholc.hoteljavaapi.repository.checkin;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.victoriholc.hoteljavaapi.model.checkin.Checkin;

/**
 * Extends the JPA CRUD methods and defines the findByNsu signature.
 *  
 * @author Victor Freitas
 * @since 03/04/2020
 */
public interface CheckinRepository extends JpaRepository<Checkin, Long>{

}
