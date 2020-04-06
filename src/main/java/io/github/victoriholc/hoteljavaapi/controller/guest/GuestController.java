package io.github.victoriholc.hoteljavaapi.controller.guest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.victoriholc.hoteljavaapi.dto.model.guest.GuestDTO;
import io.github.victoriholc.hoteljavaapi.dto.response.Response;
import io.github.victoriholc.hoteljavaapi.exception.GuestInvalidUpdateException;
import io.github.victoriholc.hoteljavaapi.exception.GuestNotFoundException;
import io.github.victoriholc.hoteljavaapi.exception.NotParsableContentException;
import io.github.victoriholc.hoteljavaapi.model.guest.Guest;
import io.github.victoriholc.hoteljavaapi.service.guest.GuestService;
import io.github.victoriholc.hoteljavaapi.util.HotelAPIUtil;
import io.swagger.annotations.ApiOperation;

/**
 * Creates all service endpoints related to the guests.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
@RestController
@RequestMapping("/hotel/guests")
public class GuestController {
	
	private static final Logger logger = LoggerFactory.getLogger(GuestController.class);

	@Autowired
	private GuestService service;
	
	/**
	 * Creates a guest in the database.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param apiVersion
	 * @param dto, where: id - guest id; hospede - guest in the hotel; 
	 * @param result - Bind result
	 * 
	 * @return ResponseEntity with a Response<CheckinDTO> object and the HTTP status
	 * 
	 * HTTP Status:
	 * 
	 * 201 - Created: Everything worked as expected.
	 * 400 - Bad Request: The request was unacceptable, often due to missing a required parameter.
	 * 403 - Forbidden: Invalid credentials to perform the request.
	 * 404 - Not Found: The requested resource doesn't exist.
	 * 409 - Conflict: The request conflicts with another request (perhaps due to using the same idempotent key).
	 * 429 - Too Many Requests: Too many requests hit the API too quickly. We recommend an exponential backoff of your requests.
	 * 500, 502, 503, 504 - Server Errors: something went wrong on API end (These are rare).
	 * 
	 * @throws NotParsableContentException
	 */
	@PostMapping
	public ResponseEntity<Response<GuestDTO>> create(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}")
				String apiVersion, @Valid @RequestBody GuestDTO dto, BindingResult result ) throws NotParsableContentException{
		
		Response<GuestDTO> response = new Response<>();
		
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		
		Guest guest = service.save(this.convertDTOToEntity(dto));
		GuestDTO dtoSaved = this.convertEntityToDTO(guest);
		this.createSelfLink(guest, dtoSaved);
		
		response.setData(dtoSaved);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		
		return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
	}

	
	
	/**
	 * Updates a guest in the database.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * 
	 * @return ResponseEntity with a Response<GuestDTO> object and the HTTP status
	 * 
	 * HTTP Status:
	 * 
	 * 200 - OK: Everything worked as expected.
	 * 400 - Bad Request: The request was unacceptable, often due to missing a required parameter.
	 * 403 - Forbidden: Invalid credentials to perform the request.
	 * 404 - Not Found: The requested resource doesn't exist.
	 * 409 - Conflict: The request conflicts with another request (perhaps due to using the same idempotent key).
	 * 429 - Too Many Requests: Too many requests hit the API too quickly. We recommend an exponential backoff of your requests.
	 * 500, 502, 503, 504 - Server Errors: something went wrong on API end (These are rare).
	 * 
	 * @throws GuestNotFoundException
	 * @throws GuestInvalidUpdateException
	 */
	@PutMapping(path = "/{id}")
	public ResponseEntity<Response<GuestDTO>> update(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}") 
		String apiVersion, @Valid @RequestBody GuestDTO dto, BindingResult result) throws GuestNotFoundException, GuestInvalidUpdateException {
		
		Response<GuestDTO> response = new Response<>();

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		Optional<Guest> transactionToFind = service.findById(dto.getId());
		if (!transactionToFind.isPresent()) {
			throw new GuestNotFoundException("Guest id: " + dto.getId() + " not found!");
		} else if (transactionToFind.get().getId().compareTo(dto.getId()) != 0) {
			throw new GuestInvalidUpdateException("You don't have permission to change the guest id: " + dto.getId() + "!");
		}

		Guest guest = service.save(this.convertDTOToEntity(dto));
		GuestDTO itemDTO = this.convertEntityToDTO(guest);
		
		this.createSelfLink(guest, itemDTO);
		response.setData(itemDTO);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}

	/**
	 * Deletes an guest by the id.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param guestId
	 * @return ResponseEntity with a Response<String> object and the HTTP status
	 * 
	 * HTTP Status:
	 * 
	 * 204 - OK: Everything worked as expected.
	 * 400 - Bad Request: The request was unacceptable, often due to missing a required parameter.
	 * 403 - Forbidden: Invalid credentials to perform the request.
	 * 404 - Not Found: The requested resource doesn't exist.
	 * 409 - Conflict: The request conflicts with another request (perhaps due to using the same idempotent key).
	 * 429 - Too Many Requests: Too many requests hit the API too quickly. We recommend an exponential backoff of your requests.
	 * 500, 502, 503, 504 - Server Errors: something went wrong on Goldgem's end (These are rare).
	 * 
	 * @throws GuestNotFoundException 
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> delete(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}") 
		String apiVersion, @PathVariable("id") Long guestId) throws GuestNotFoundException {
		
		Response<String> response = new Response<>();
		Optional<Guest> guest = service.findById(guestId);
		
		if (!guest.isPresent()) {
			throw new GuestNotFoundException("Guest id: " + guestId + " not found!");
		}
		
		service.deleteById(guestId);
		response.setData("Guest id: " + guestId + " successfully deleted!");
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		return new ResponseEntity<>(response, headers, HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Method that searches a guest by the id.
	 * 
	 * @param apiVersion
	 * @param guestId
	 * @return ResponseEntity with a Response<GuestDTO> object and the HTTP status
	 * 
	 * HTTP Status:
	 * 
	 * 200 - OK: Everything worked as expected.
	 * 400 - Bad Request: The request was unacceptable, often due to missing a required parameter.
	 * 403 - Forbidden: Invalid credentials to perform the request.
	 * 404 - Not Found: The requested resource doesn't exist.
	 * 409 - Conflict: The request conflicts with another request (perhaps due to using the same idempotent key).
	 * 429 - Too Many Requests: Too many requests hit the API too quickly. We recommend an exponential backoff of your requests.
	 * 500, 502, 503, 504 - Server Errors: something went wrong on API end (These are rare).
	 * 
	 * @throws GuestNotFoundException
	 */
	@GetMapping(value = "/{id}")
	@ApiOperation(value = "Route to find a guest by your id in the API")
	public ResponseEntity<Response<GuestDTO>> findById(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}") 
		String apiVersion, @PathVariable("id") Long guestId) throws GuestNotFoundException {
		
		Response<GuestDTO> response = new Response<>();
		Optional<Guest> guest = service.findById(guestId);
		
		if (!guest.isPresent()) {
			throw new GuestNotFoundException("Guest id: " + guestId + " not found!");
		}
		
		GuestDTO dto = convertEntityToDTO(guest.get());
		createSelfLink(guest.get(), dto);
		response.setData(dto);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	
	/**
	 * Searches for all the guests saved in or out the hotel.
	 *  
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @return ResponseEntity with a Response<List<GuestDTO>> object and the HTTP status
	 * 
	 * HTTP Status:
	 * 
	 * 204 - OK: Everything worked as expected.
	 * 400 - Bad Request: The request was unacceptable, often due to missing a required parameter.
	 * 403 - Forbidden: Invalid credentials to perform the request.
	 * 404 - Not Found: The requested resource doesn't exist.
	 * 409 - Conflict: The request conflicts with another request (perhaps due to using the same idempotent key).
	 * 429 - Too Many Requests: Too many requests hit the API too quickly. We recommend an exponential backoff of your requests.
	 * 500, 502, 503, 504 - Server Errors: something went wrong on API end (These are rare).
	 * 
	 * @throws GuestNotFoundException 
	 */
	
	@GetMapping(value = "/bySaida/{saiu}")
	public ResponseEntity<Response<List<GuestDTO>>> findInOutHotel(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}") 
		String apiVersion, @PathVariable("saiu") String isOut) throws GuestNotFoundException {
		
		Response<List<GuestDTO>> response = new Response<>();
		List<Guest> guests = new ArrayList<>();
		
		if (isOut.equals("in")) {
			guests = service.findInHotel();
		} else if (isOut.equals("out")) {
			guests = service.findOutHotel();
		}
		
		if (guests.isEmpty()) {
			throw new GuestNotFoundException("There are no guests registered in the database!");
		}
		
		List<GuestDTO> itemsDTO = new ArrayList<>();
		guests.stream().forEach(i -> itemsDTO.add(this.convertEntityToDTO(i)));
		
		itemsDTO.stream().forEach(dto -> {
			try {
				this.createSelfLinkInCollections(apiVersion, dto);
			} catch (GuestNotFoundException e) {
				logger.error("There are no guests registered in the database!");
			}
		});
		
		response.setData(itemsDTO);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	
	/**
	 * Searches for all the guests saved.
	 *  
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @return ResponseEntity with a Response<List<TransactionDTO>> object and the HTTP status
	 * 
	 * HTTP Status:
	 * 
	 * 204 - OK: Everything worked as expected.
	 * 400 - Bad Request: The request was unacceptable, often due to missing a required parameter.
	 * 403 - Forbidden: Invalid credentials to perform the request.
	 * 404 - Not Found: The requested resource doesn't exist.
	 * 409 - Conflict: The request conflicts with another request (perhaps due to using the same idempotent key).
	 * 429 - Too Many Requests: Too many requests hit the API too quickly. We recommend an exponential backoff of your requests.
	 * 500, 502, 503, 504 - Server Errors: something went wrong on API end (These are rare).
	 * 
	 * @throws GuestNotFoundException 
	 */
	@GetMapping
	public ResponseEntity<Response<List<GuestDTO>>> findAll(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}") 
		String apiVersion) throws GuestNotFoundException {
		
		Response<List<GuestDTO>> response = new Response<>();
		
		
		List<Guest> guests = service.findAll();
		
		if (guests.isEmpty()) {
			throw new GuestNotFoundException("There are no guests registered in the database!");
		}
		
		List<GuestDTO> itemsDTO = new ArrayList<>();
		guests.stream().forEach(i -> itemsDTO.add(this.convertEntityToDTO(i)));
		
		itemsDTO.stream().forEach(dto -> {
			try {
				this.createSelfLinkInCollections(apiVersion, dto);
			} catch (GuestNotFoundException e) {
				logger.error("There are no transactions registered in the database!");
			}
		});
		
		response.setData(itemsDTO);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	
	/**
	 * Converts an Guest DTO to an Guest entity.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param dto
	 * @return a Guest object
	 */
	private Guest convertDTOToEntity(GuestDTO dto) {
		
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(dto, Guest.class);
	}
	
	/**
	 * Converts an Guest entity to an Guest DTO.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param guest
	 * @return a GuestDTO object
	 */
	private GuestDTO convertEntityToDTO(Guest guest) {
		
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(guest, GuestDTO.class);
	}
	
	/**
	 * Creates a self link to guest object
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param guests
	 * @param guestDTO
	 */
	private void createSelfLink(Guest guests, GuestDTO guestDTO) {
		Link selfLink = WebMvcLinkBuilder.linkTo(GuestController.class).slash(guests.getId()).withSelfRel();
		guestDTO.add(selfLink);
	}
	/**
	 * Creates a self link in a collection of guests
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param apiVersion
	 * @param guestDTO
	 * @throws GuestNotFoundException
	 */
	private void createSelfLinkInCollections(String apiVersion, final GuestDTO guestDTO) throws GuestNotFoundException {
		Link selfLink = linkTo(methodOn(GuestController.class).findById(apiVersion, guestDTO.getId())).withSelfRel();
		guestDTO.add(selfLink);
	}
}
