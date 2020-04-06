package io.github.victoriholc.hoteljavaapi.model.guest;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Guest structure.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */

@Entity
@Table(name = "guests")
@AllArgsConstructor
@NoArgsConstructor
public class Guest implements Serializable {

	private static final long serialVersionUID = 5664509607101543997L;
	
	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Getter
	@Setter
	@Column(name = "nome")
	private String nome;
	@Getter
	@Setter
	@Column(name = "documento")
	private String documento;
	@Getter
	@Setter
	@Column(name = "telefone")
	private String telefone;
	
	@Getter
	@Setter
	@Column(name = "valor_gasto")
	private BigDecimal valorGasto;

}
