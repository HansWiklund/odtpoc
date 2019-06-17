package se.inera.odp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.inera.odp.request.ODPRequest;
import se.inera.odp.service.CKANService;

// @RequestMapping(value = "/greeting", method = POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

@RestController
@RequestMapping({"/api"})
public class ODPController {

	@Autowired
	CKANService ckanService;
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getDataById(@PathVariable String id) {

		ResponseEntity<String> data = ckanService.getDataById(id);
		return data;
	}
	
	@PostMapping()
	public ResponseEntity<?> createData(@RequestBody ODPRequest data) {
		//TODO
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
}
