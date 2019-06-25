package se.inera.odp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import opendata.followup.groupoutcomes.qualitymeasures._2.Codes;
import se.inera.odp.request.ODPRequest;
import se.inera.odp.service.MockAdapterService;

// @RequestMapping(value = "/greeting", method = POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

@RestController
@RequestMapping({"/api/kik/v1"})
public class MockAdapterController {

	@Autowired
	MockAdapterService mockAdapterService;
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody Codes data) {
		//TODO
		mockAdapterService.saveDataById(data);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
}
