package se.inera.odp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.inera.odp.service.ODPService;

// @RequestMapping(value = "/greeting", method = POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

@RestController
@RequestMapping({"/api"})
public class ODPController {

	Logger logger = LoggerFactory.getLogger(ODPController.class);

	@Autowired
	ODPService ckanService;
	
	@GetMapping("/get/{id}")
	public ResponseEntity<String> getResourceById(@PathVariable String id) {

		String result = ckanService.getResourceById(id);
		if(result == null)
			return new ResponseEntity<String>(result, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> createResource(@RequestHeader(value="Authorization") String auth, @RequestHeader(value="Content-Type") String contentType, @RequestBody String data) {

		try {
			ckanService.createResource(auth, contentType, data);
			logger.info("Request was succesfully saved!");
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch(Exception e) {
			logger.error("An error occured during save", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
