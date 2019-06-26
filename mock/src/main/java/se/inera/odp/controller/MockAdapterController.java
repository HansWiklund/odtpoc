package se.inera.odp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	Logger logger = LoggerFactory.getLogger(MockAdapterController.class);
	
	@Autowired
	MockAdapterService mockAdapterService;
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody Codes data) {
		try {
			mockAdapterService.saveResource(data);
			logger.info("Request was succesfully saved!");
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch(Exception e) {
			logger.error("An error occured during save", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody CodeSystems data) {
		try {
			mockAdapterService.saveResource(data);
			logger.info("Request was succesfully saved!");
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch(Exception e) {
			logger.error("An error occured during save", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody Measures data) {
		try {
			mockAdapterService.saveResource(data);
			logger.info("Request was succesfully saved!");
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch(Exception e) {
			logger.error("An error occured during save", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody MeasureFormerVersions data) {
		try {
			mockAdapterService.saveResource(data);
			logger.info("Request was succesfully saved!");
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch(Exception e) {
			logger.error("An error occured during save", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody PerformingOrganizations data) {
		try {
			mockAdapterService.saveResource(data);
			logger.info("Request was succesfully saved!");
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch(Exception e) {
			logger.error("An error occured during save", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody TargetMeasurements data) {
		try {
			mockAdapterService.saveResource(data);
			logger.info("Request was succesfully saved!");
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch(Exception e) {
			logger.error("An error occured during save", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<?> createData(@RequestBody ValueSets data) {
		try {
			mockAdapterService.saveResource(data);
			logger.info("Request was succesfully saved!");
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch(Exception e) {
			logger.error("An error occured during save", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
