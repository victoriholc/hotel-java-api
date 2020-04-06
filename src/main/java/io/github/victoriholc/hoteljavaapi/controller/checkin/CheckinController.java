package io.github.victoriholc.hoteljavaapi.controller.checkin;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.victoriholc.hoteljavaapi.controller.guest.GuestController;
import io.github.victoriholc.hoteljavaapi.dto.model.checkin.CheckinDTO;
import io.github.victoriholc.hoteljavaapi.dto.model.guest.GuestDTO;
import io.github.victoriholc.hoteljavaapi.dto.response.Response;
import io.github.victoriholc.hoteljavaapi.exception.NotParsableContentException;
import io.github.victoriholc.hoteljavaapi.model.checkin.Checkin;
import io.github.victoriholc.hoteljavaapi.model.guest.Guest;
import io.github.victoriholc.hoteljavaapi.service.checkin.CheckinService;
import io.github.victoriholc.hoteljavaapi.service.guest.GuestService;
import io.github.victoriholc.hoteljavaapi.exception.CheckinNotFoundException;
import io.github.victoriholc.hoteljavaapi.exception.GuestNotFoundException;
import io.github.victoriholc.hoteljavaapi.util.HotelAPIUtil;
import io.swagger.annotations.ApiOperation;

/**
 * Creates all service endpoints related to the check in.
 * 
 * @author Victor Freitas
 * @since 03/04/2020
 */
@RestController
@RequestMapping("/hotel/checkins")
public class CheckinController {

	private static final Logger logger = LoggerFactory.getLogger(CheckinController.class);
	
	@Autowired
	private CheckinService checkinService;
	@Autowired
	private GuestService guestService;
	
	/**
	 * Creates a checkin in the database.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param apiVersion
	 * @param dto, where: id - checkin id; hospede - guest in the hotel; dataEntrada - day and time the guest got in, in the ISO 8601 format YYYY-MM-DDThh:mm:ss.sssZ in the Local timezone;
	 * dataSaida - day and time the guest got out, in the ISO 8601 format YYYY-MM-DDThh:mm:ss.sssZ in the Local timezone; 
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
	public ResponseEntity<Response<CheckinDTO>> save(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}")
				String apiVersion, @Valid @RequestBody CheckinDTO dto, BindingResult result ) throws NotParsableContentException{
		
		Response<CheckinDTO> response = new Response<>();
		
		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.addErrorMsgToResponse(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		
		Checkin checkin = checkinService.save(this.convertDTOToCheckin(dto));
		CheckinDTO dtoSaved = this.convertCheckinToDTO(checkin);
		this.createSelfLinkCheckin(checkin, dtoSaved);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		
		calculateExpenses(checkinService.findAll());
		
		return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
	}

	/**
	 * Method that searches a guest by the id.
	 * 
	 * @param apiVersion
	 * @param checkinId
	 * @return ResponseEntity with a Response<CheckinDTO> object and the HTTP status
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
	 * @throws CheckinNotFoundException
	 */
	@GetMapping(value = "/{id}")
	@ApiOperation(value = "Route to find a checkin by your id in the API")
	public ResponseEntity<Response<CheckinDTO>> findById(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}") 
		String apiVersion, @PathVariable("id") Long checkinId) throws CheckinNotFoundException {
		
		Response<CheckinDTO> response = new Response<>();
		Optional<Checkin> checkin = checkinService.findById(checkinId);
		
		if (!checkin.isPresent()) {
			throw new CheckinNotFoundException("Checkin id: " + checkinId + " not found!");
		}
		
		CheckinDTO dto = convertCheckinToDTO(checkin.get());
		createSelfLinkCheckin(checkin.get(), dto);
		response.setData(dto);
		
		calculateExpenses(checkinService.findAll());
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	
	/**
	 * Searches for all the guests given a nome.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param apiVersion
	 * @param guestNome
	 * @return ResponseEntity with a Response<String> object and the HTTP status
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
	@GetMapping(value = "/byNome/{nome}")
	public ResponseEntity<Response<List<GuestDTO>>> findByNome(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}") 
		String apiVersion, @PathVariable("nome") String guestNome) throws GuestNotFoundException {
		
		Response<List<GuestDTO>> response = new Response<>();
		List<Guest> guests = guestService.findByNome(guestNome);
		
		if (guests.isEmpty()) {
			throw new GuestNotFoundException("There are no guests registered with the nome :" + guestNome + "!");
		}
		
		List<GuestDTO> guestsDTO = new ArrayList<>();
		guests.stream().forEach(t -> guestsDTO.add(this.convertGuestToDTO(t)));
		
		guestsDTO.stream().forEach(dto -> {
			try {
				this.createSelfLinkInGuestCollections(apiVersion, dto);
			} catch (GuestNotFoundException e) {
				logger.error("There are no guests registered with the nome: " + guestNome + "!");
			}
		});
		
		response.setData(guestsDTO);
		
		calculateExpenses(checkinService.findAll());
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	
	/**
	 * Searches for all the guests given a documento.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param apiVersion
	 * @param guestNome
	 * @return ResponseEntity with a Response<String> object and the HTTP status
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
	@GetMapping(value = "/byDocumento/{documento}")
	public ResponseEntity<Response<List<GuestDTO>>> findByDocumento(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}") 
		String apiVersion, @PathVariable("documento") String guestDocumento) throws GuestNotFoundException {
		
		Response<List<GuestDTO>> response = new Response<>();
		List<Guest> guests = guestService.findByDocumento(guestDocumento);
		
		if (guests.isEmpty()) {
			throw new GuestNotFoundException("There are no guests registered with the documento :" + guestDocumento + "!");
		}
		
		List<GuestDTO> guestsDTO = new ArrayList<>();
		guests.stream().forEach(t -> guestsDTO.add(this.convertGuestToDTO(t)));
		
		guestsDTO.stream().forEach(dto -> {
			try {
				this.createSelfLinkInGuestCollections(apiVersion, dto);
			} catch (GuestNotFoundException e) {
				logger.error("There are no guests registered with the documento: " + guestDocumento + "!");
			}
		});
		
		response.setData(guestsDTO);
		
		calculateExpenses(checkinService.findAll());
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	/**
	 * Searches for all the guests given a telefone.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param apiVersion
	 * @param guestNome
	 * @return ResponseEntity with a Response<String> object and the HTTP status
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
	@GetMapping(value = "/byTelefone/{telefone}")
	public ResponseEntity<Response<List<GuestDTO>>> findByTelefone(@RequestHeader(value=HotelAPIUtil.HOTEL_API_VERSION_HEADER, defaultValue="${api.version}") 
		String apiVersion, @PathVariable("telefone") String guestTelefone) throws GuestNotFoundException {
		
		Response<List<GuestDTO>> response = new Response<>();
		List<Guest> guests = guestService.findByTelefone(guestTelefone);
		
		if (guests.isEmpty()) {
			throw new GuestNotFoundException("There are no guests registered with the telefone :" + guestTelefone + "!");
		}
		
		List<GuestDTO> guestsDTO = new ArrayList<>();
		guests.stream().forEach(t -> guestsDTO.add(this.convertGuestToDTO(t)));
		
		guestsDTO.stream().forEach(dto -> {
			try {
				this.createSelfLinkInGuestCollections(apiVersion, dto);
			} catch (GuestNotFoundException e) {
				logger.error("There are no guests registered with the telefone: " + guestTelefone + "!");
			}
		});
		
		response.setData(guestsDTO);
		
		calculateExpenses(checkinService.findAll());
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add(HotelAPIUtil.HOTEL_API_VERSION_HEADER, apiVersion);
		return new ResponseEntity<>(response, headers, HttpStatus.OK);
	}
	
	/**
	 * Creates the guest's expenses by the list of checkins in the database.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param checkins
	 * @return a BigDecimal object
	 */
	private void calculateExpenses(List<Checkin> checkins) {
		
		for (Checkin checkin : checkins ) {
			Date d1 = checkin.getDataEntrada();
			Date d2 = checkin.getDataSaida();
			
	        Calendar c1 = Calendar.getInstance();
	        c1.setTime(d1);

	        Calendar c2 = Calendar.getInstance();
	        c2.setTime(d2);
	        
	        BigDecimal total = new BigDecimal(0);
	        
	        while (! c1.after(c2)) {
	        	
	            if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ){
	            	total = total.add(new BigDecimal(150));
	                if (checkin.getAdicionalVeiculo()) {
	                	total = total.add(new BigDecimal(20));
		            }
	            }else if(c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
	            	total = total.add(new BigDecimal(150));
	            	if (checkin.getAdicionalVeiculo()) {
	            		total = total.add(new BigDecimal(20));
		            }
	            } else {
	            	total = total.add(new BigDecimal(120));
	            	if (checkin.getAdicionalVeiculo()) {
	            		total = total.add(new BigDecimal(15));
		            }
	            }
	            
	            c1.add(Calendar.DATE, 1);
	        }
	        
	        if (c2.get(Calendar.HOUR_OF_DAY) >= 16 && c2.get(Calendar.MINUTE) > 30 ) {
	        	if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ){
	        		total = total.add(new BigDecimal(150));
	            }else if(c2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
	            	total = total.add(new BigDecimal(150));
	            } else {
	            	total = total.add(new BigDecimal(120));
	            }
	        	
	        }
	        
	        System.out.println(total);
	        checkin.getHospede().setValorGasto(total);
	        guestService.save(checkin.getHospede());
		}
	}
	
	
	/**
	 * Converts an Checkin DTO to an Checkin entity.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param dto
	 * @return a Checkin object
	 */
	private Checkin convertDTOToCheckin(CheckinDTO dto) {
		
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(dto, Checkin.class);
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
	private Guest convertDTOToGuest(GuestDTO dto) {
		
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(dto, Guest.class);
	}

	/**
	 * Converts an Checkin entity to an Checkin DTO.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param transaction
	 * @return a TransactionDTO object
	 */
	private CheckinDTO convertCheckinToDTO(Checkin checkin) {
		
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(checkin, CheckinDTO.class);
	}
	
	/**
	 * Converts an Guest entity to an Checkin DTO.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param transaction
	 * @return a TransactionDTO object
	 */
	private GuestDTO convertGuestToDTO(Guest guest) {
		
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(guest, GuestDTO.class);
	}
	
	/**
	 * Creates a self link to checkin object
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param checkin
	 * @param checkinDTO
	 */
	private void createSelfLinkCheckin(Checkin checkin, CheckinDTO checkinDTO) {
		Link selfLink = WebMvcLinkBuilder.linkTo(CheckinController.class).slash(checkin.getId()).withSelfRel();
		checkinDTO.add(selfLink);
	}
	
	/**
	 * Creates a self link to guest object
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param guest
	 * @param guestDTO
	 */
	private void createSelfLinkGuest(Guest guest, GuestDTO guestDTO) {
		Link selfLink = WebMvcLinkBuilder.linkTo(CheckinController.class).slash(guest.getId()).withSelfRel();
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
	private void createSelfLinkInGuestCollections(String apiVersion, final GuestDTO guestDTO) throws GuestNotFoundException {
		Link selfLink = linkTo(methodOn(GuestController.class).findById(apiVersion, guestDTO.getId())).withSelfRel();
		guestDTO.add(selfLink);
	}
	/**
	 * Creates a self link in a collection of checkins
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @param apiVersion
	 * @param guestDTO
	 * @throws GuestNotFoundException
	 */
	private void createSelfLinkInCheckinCollections(String apiVersion, final CheckinDTO checkinDTO) throws CheckinNotFoundException {
		Link selfLink = linkTo(methodOn(CheckinController.class).findById(apiVersion, checkinDTO.getId())).withSelfRel();
		checkinDTO.add(selfLink);
	}
}
