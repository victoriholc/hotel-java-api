package io.github.victoriholc.hoteljavaapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ServerErrorException;

import com.fasterxml.jackson.core.JsonParseException;

import io.github.victoriholc.hoteljavaapi.dto.response.Response;

/**
 * Handler of exceptions and errors in the API, using {@ControllerAdvice} 
 * also sends the proper response to the client.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 *
 * @param <T>
 */

public class HotelJavaApiExceptionHandler<T> {

	/**
	 * Handles the TransactionInvalidUpdateException and returns an error with 
	 * status code = 403.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param exception
	 * @param request
	 * @return ResponseEntity<Response<T>>
	 */
	@ExceptionHandler(value = {GuestInvalidUpdateException.class})
	protected ResponseEntity<Response<T>> handleGuestInvalidUpdateException(GuestInvalidUpdateException exception,
			WebRequest request){
		
		Response<T> response = new Response<>();
		response.addErrorMsgToResponse(exception.getLocalizedMessage());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}
	
	
	/**
	 * Handles the GuestNotFoundException and returns an error with 
	 * status code = 404.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param exception
	 * @param request
	 * @return ResponseEntity<Response<T>>
	 */
	@ExceptionHandler(value = {GuestNotFoundException.class})
	protected ResponseEntity<Response<T>> handleGuestNotFoundException(GuestNotFoundException exception,
			WebRequest request){
		
		Response<T> response = new Response<>();
		response.addErrorMsgToResponse(exception.getLocalizedMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	/**
	 * Handles the GuestNotFoundException and returns an error with 
	 * status code = 404.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param exception
	 * @param request
	 * @return ResponseEntity<Response<T>>
	 */
	@ExceptionHandler(value = {GuestNotFoundException.class})
	protected ResponseEntity<Response<T>> handleCheckinNotFoundException(CheckinNotFoundException exception,
			WebRequest request){
		
		Response<T> response = new Response<>();
		response.addErrorMsgToResponse(exception.getLocalizedMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	/**
	 * Handles the HttpClientErrorException and returns an Conflict
	 * error with status code = 409.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param exception
	 * @param request
	 * @return ResponseEntity<Response<T>>
	 */
	@ExceptionHandler(value = {HttpClientErrorException.Conflict.class})
	protected ResponseEntity<Response<T>> handleConflictExcetion(HttpClientErrorException exception,
			WebRequest request){
		
		Response<T> response = new Response<>();
		response.addErrorMsgToResponse(exception.getLocalizedMessage());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	

	/**
	 * Handles the HttpMessageNotReadableException or JsonParseException and 
	 * returns an Unprocessable Entity error with status code = 422.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param exception
	 * @param request
	 * @return ResponseEntity<Response<T>>
	 */
	@ExceptionHandler(value = {HttpMessageNotReadableException.class, JsonParseException.class, NotParsableContentException.class})
	protected ResponseEntity<Response<T>> handleMessageNotReadableException(Exception exception, WebRequest request){
		
		Response<T> response = new Response<>();
		response.addErrorMsgToResponse(exception.getLocalizedMessage());
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
	}
	
	/**
	 * Handles the HttpClientErrorException and returns an TooManyRequests error 
	 * with status code = 429.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param exception
	 * @param request
	 * @return ResponseEntity<Response<T>>
	 */
	@ExceptionHandler(value = {HttpClientErrorException.TooManyRequests.class})
	protected ResponseEntity<Response<T>> handleTooManyRequestsException(HttpClientErrorException exception, 
			WebRequest request){
		
		Response<T> response = new Response<>();
		response.addErrorMsgToResponse(exception.getLocalizedMessage());
		
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
	}
	
	/**
	 * Handles the GoldgemException and returns an Internal Server Error 
	 * with status code = 500.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param exception
	 * @param request
	 * @return ResponseEntity<Response<T>>
	 */
	@ExceptionHandler(value = {ServerErrorException.class})
	protected ResponseEntity<Response<T>> handleAPIException(ServerErrorException exception, WebRequest request){
		
		Response<T> response = new Response<>();
		response.addErrorMsgToResponse(exception.getLocalizedMessage());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
