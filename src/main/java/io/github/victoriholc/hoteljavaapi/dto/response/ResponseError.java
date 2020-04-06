package io.github.victoriholc.hoteljavaapi.dto.response;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Generic response error object to the API endpoints.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
@Accessors(chain = true)
@NoArgsConstructor
public class ResponseError {

	@Getter
	@Setter
	@NotNull(message = "Timestamp cannot be null")
	private LocalDateTime timestamp;
	
	@Getter
	@Setter
	@NotNull(message = "Details cannot be null")
	private String details;
}
