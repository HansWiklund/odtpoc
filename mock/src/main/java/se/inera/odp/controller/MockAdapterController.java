package se.inera.odp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import opendata.followup.groupoutcomes.qualitymeasures._2.*;
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
		mockAdapterService.saveResource(data);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody CodeSystems data) {
		//TODO
		mockAdapterService.saveResource(data);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody Measures data) {
		//TODO
		mockAdapterService.saveResource(data);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody MeasureFormerVersions data) {
		//TODO
		mockAdapterService.saveResource(data);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody PerformingOrganizations data) {
		//TODO
		mockAdapterService.saveResource(data);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody TargetMeasurements data) {
		//TODO
		mockAdapterService.saveResource(data);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody ValueSets data) {
		//TODO
		mockAdapterService.saveResource(data);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

}
