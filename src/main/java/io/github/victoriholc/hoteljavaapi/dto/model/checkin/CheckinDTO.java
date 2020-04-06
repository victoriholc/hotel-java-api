package io.github.victoriholc.hoteljavaapi.dto.model.checkin;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.victoriholc.hoteljavaapi.model.guest.Guest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Checkin data transfer object (DTO)
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
@EqualsAndHashCode(callSuper = false)
public class CheckinDTO extends RepresentationModel<CheckinDTO>{

	@Getter
	@Setter
	private Long id;
	@Getter
	@Setter
	@NotNull(message = "Hospede cannot be null")
	private Guest hospede;
	@Getter
	@Setter
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@NotNull(message = "Data entrada cannot be null")
	private Date dataEntrada;
	@Getter
	@Setter
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@NotNull(message="Data saida cannot be null")
	private Date dataSaida;
	@Getter
	@Setter
	@NotNull(message = "Adicional veiculo cannot be null")
	private boolean adicionalVeiculo;
}
