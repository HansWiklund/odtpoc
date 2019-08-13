package se.inera.odp.controller;

import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.inera.odp.core.exception.ODPAuthorizationException;
import se.inera.odp.core.utils.ResponseLoggerMapper;
import se.inera.odp.service.ODPService;

// @RequestMapping(value = "/greeting", method = POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

@RestController
@RequestMapping({"/api"})
public class ODPController {

	Logger logger = LoggerFactory.getLogger(ODPController.class);

	private static final String AUTHORIZATION = "Authorization";
	
	@Autowired
	private ODPService ckanService;

	@Autowired
	private ResponseLoggerMapper responseMapper;

	@GetMapping("/get/{dataset_id}/{resource_id}")
	public ResponseEntity<String> getResourceById(
			@RequestHeader(value=AUTHORIZATION, required=false ) String auth, 
			@PathVariable String dataset_id, @PathVariable String resource_id,
			@RequestParam Map<String,String> params) {
		
		String url = "/api/get/" + dataset_id + "/" + resource_id;
		String result = ckanService.getResourceById(dataset_id, resource_id, params, auth);
		responseMapper.responseMessage(HttpStatus.OK, "OK", url);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@PostMapping(value="/save")
	public ResponseEntity<String> createResource(
			@RequestHeader(value=AUTHORIZATION, required=false ) String auth, 
			@RequestBody String data) {
 
		if(auth == null)
			throw new ODPAuthorizationException();
			
		String url = "/api/get/save";
		String result = ckanService.createResource(auth, data);
		responseMapper.responseMessage(HttpStatus.OK, "OK", url);
		return new ResponseEntity<String>(result, HttpStatus.CREATED);
	}
	
	@GetMapping("/ping")
	public ResponseEntity<String> getPingResponse() {
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}	
	
	@DeleteMapping("/delete/{dataset_id}/{resource_id}")
	public ResponseEntity<String> deleteResource(
			@RequestHeader(value=AUTHORIZATION) String auth, 
			@PathVariable String dataset_id, @PathVariable String resource_id) throws IOException {

		String url = "/api/delete/" + dataset_id + "/" + resource_id;
		String response = ckanService.deleteResource(auth, dataset_id, resource_id);
		responseMapper.responseMessage(HttpStatus.OK, "OK", url);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	@PutMapping("/update/{dataset_id}/{resource_id}")
	public ResponseEntity<String> updateResource(
			@RequestHeader(value=AUTHORIZATION) String auth, 
			@PathVariable String dataset_id, @PathVariable String resource_id,
			@RequestBody String data) throws IOException {

		String url = "/api/update/" + dataset_id + "/" + resource_id;
		String response = ckanService.updateResource(auth, dataset_id, resource_id, data);
		responseMapper.responseMessage(HttpStatus.OK, "OK", url);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
}
