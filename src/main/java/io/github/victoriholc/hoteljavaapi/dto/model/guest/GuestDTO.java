package io.github.victoriholc.hoteljavaapi.dto.model.guest;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Guest data transfer object (DTO)
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
@EqualsAndHashCode(callSuper = false)
public class GuestDTO extends RepresentationModel<GuestDTO> {

	@Getter
	@Setter
	private Long id;
	@Getter
	@Setter
	@NotNull(message="Nome cannot be null")
	@Length(min = 3, message = "Nome must countain at least 3 chars")
	private String nome;
	@Getter
	@Setter
	@NotNull(message="Documento cannot be null")
	private String documento;
	@Getter
	@Setter
	@NotNull(message="Telefone cannot be null")
	private String telefone;
	@Getter
	@Setter
	private BigDecimal valorGasto;
}
