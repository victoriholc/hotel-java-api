package io.github.victoriholc.hoteljavaapi.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Generic response to the API endpoints.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 *
 * @param <T>
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

	@Getter
	@Setter
	private T data;
	@Getter
	@Setter
	private Object error;
	
	/**
	 * Formats an error message to the HTTP response.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param errorMsg
	 */
	public void addErrorMsgToResponse(String errorMsg) {
		ResponseError error = new ResponseError().setDetails(errorMsg)
				.setTimestamp(LocalDateTime.now());
		this.setError(error);
	}
}
