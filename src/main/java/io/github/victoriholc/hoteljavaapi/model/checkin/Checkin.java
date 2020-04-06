package io.github.victoriholc.hoteljavaapi.model.checkin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.github.victoriholc.hoteljavaapi.model.guest.Guest;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Check in structure.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */

@Entity
@Table(name = "checkins")
@AllArgsConstructor
@NoArgsConstructor
public class Checkin implements Serializable{

	private static final long serialVersionUID = -4404377393434209028L;
	
	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Getter
	@Setter
	@JoinColumn(name = "hospede", referencedColumnName = "id")
	@OneToOne(cascade = {CascadeType.MERGE})
	@JsonProperty(access = Access.READ_WRITE)
	private Guest hospede;
	@Getter
	@Setter
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@Column(name = "dataEntrada")
	private Date dataEntrada;
	@Getter
	@Setter
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@Column(name = "dataSaida")
	private Date dataSaida;
	@Getter
	@Setter
	@Column(name = "adicionalVeiculo")
	private Boolean adicionalVeiculo;
}
