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
import se.inera.odp.service.ODPService;

// @RequestMapping(value = "/greeting", method = POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

@RestController
@RequestMapping({"/api"})
public class ODPController {

	Logger logger = LoggerFactory.getLogger(ODPController.class);

	@Autowired
	ODPService ckanService;
	
	@GetMapping("/get/{dataset_id}/{resource_id}")
	public ResponseEntity<String> getResourceById(
			@RequestHeader(value="Authorization", required=false ) String auth, 
			@PathVariable String dataset_id, @PathVariable String resource_id,
			@RequestParam Map<String,String> params) {

		String result;
		try {
			result = ckanService.getResourceById(dataset_id, resource_id, params, auth);
			if(result == null)
				return new ResponseEntity<String>(result, HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<String>(result, HttpStatus.OK);
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);			
		}
	}
	
	@PostMapping(value="/save")
	public ResponseEntity<?> createResource(
			@RequestHeader(value="Authorization", required=false ) String auth, 
			@RequestBody String data) {
 
		if(auth == null)
			throw new ODPAuthorizationException();
			
		try {
			ckanService.createResource(auth, data);
			logger.info("Request was succesfully saved!");
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch(RuntimeException e) {
			logger.error("An error occured during save", e);
			throw e;
		}
	}
	
	@GetMapping("/ping")
	public ResponseEntity<String> getPingResponse() {
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}	
	
	@DeleteMapping("/delete/{dataset_id}/{resource_id}")
	public ResponseEntity<?> deleteResource(
			@RequestHeader(value="Authorization") String auth, 
			@PathVariable String dataset_id, @PathVariable String resource_id) throws IOException {

			boolean bool = ckanService.deleteResource(auth, dataset_id, resource_id);
			if (bool) {
				return new ResponseEntity<String>(HttpStatus.OK);
			}
			else {
				logger.error("An error occured during deleting resource");
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
	}
	
	@PutMapping("/update/{dataset_id}/{resource_id}")
	public ResponseEntity<?> updateResource(
			@RequestHeader(value="Authorization") String auth, 
			@PathVariable String dataset_id, @PathVariable String resource_id,
			@RequestBody String data) throws IOException {

			boolean bool = ckanService.updateResource(auth, dataset_id, resource_id, data);
			if (bool) {
				return new ResponseEntity<String>(HttpStatus.OK);
			}
			else {
				logger.error("An error occured during deleting resource");
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
	}
	
}
