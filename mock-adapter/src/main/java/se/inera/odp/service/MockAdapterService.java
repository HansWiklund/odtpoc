package se.inera.odp.service;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import opendata.followup.groupoutcomes.qualitymeasures._2.*;
import se.inera.odp.client.AdapterClient;
import se.inera.odp.core.exception.ODPException;
import se.inera.odp.core.request.ODPRequest;

@Service
public class MockAdapterService {

	Logger logger = LoggerFactory.getLogger(MockAdapterService.class);

	@Autowired
	AdapterClient adapterClient;

	@Autowired
	ObjectMapper mapper;

	private static final String KIK_CODES_DEFINITION = "kik/definition_kik_v1_codes.json";
	
	public void saveResource(String auth, Codes data, HttpHeaders headers) {	
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kik/definition_kik_v1_codes.json");
			ODPRequest<CodeType> req = mapper.readValue(is, ODPRequest.class);
			req.setRecords(data.getCode());
			adapterClient.createResource(auth, headers, req, ODPRequest.class);
		} catch (IOException e) {
			throw new ODPException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public void saveResource(String auth, CodeSystems data, HttpHeaders headers) {	
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kik/definition_kik_v1_codes.json");
			ODPRequest<CodeSystemType> req = mapper.readValue(is, ODPRequest.class);
			req.setRecords(data.getCodeSystem());
			adapterClient.createResource(auth, headers, req, ODPRequest.class);
		} catch (IOException e) {
			throw new ODPException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public void saveResource(String auth, Measures data, HttpHeaders headers) {	
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kik/definition_kik_v1_codes.json");
			ODPRequest<MeasureType> req = mapper.readValue(is, ODPRequest.class);
			req.setRecords(data.getMeasure());
			adapterClient.createResource(auth, headers, req, ODPRequest.class);
		} catch (IOException e) {
			throw new ODPException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public void saveResource(String auth, MeasureFormerVersions data, HttpHeaders headers) {	
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kik/definition_kik_v1_codes.json");
			ODPRequest<MeasureFormerVersionType> req = mapper.readValue(is, ODPRequest.class);
			req.setRecords(data.getMeasureFormerVersion());
			adapterClient.createResource(auth, headers, req, ODPRequest.class);
		} catch (IOException e) {
			throw new ODPException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public void saveResource(String auth, TargetMeasurements data, HttpHeaders headers) {	
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kik/definition_kik_v1_codes.json");
			ODPRequest<TargetMeasurementType> req = mapper.readValue(is, ODPRequest.class);
			req.setRecords(data.getTargetMeasurement());
			adapterClient.createResource(auth, headers, req, ODPRequest.class);
		} catch (IOException e) {
			throw new ODPException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public void saveResource(String auth, PerformingOrganizations data, HttpHeaders headers) {	
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kik/definition_kik_v1_codes.json");
			ODPRequest<PerformingOrganizationType> req = mapper.readValue(is, ODPRequest.class);
			req.setRecords(data.getPerformingOrganization());
			adapterClient.createResource(auth, headers, req, ODPRequest.class);
		} catch (IOException e) {
			throw new ODPException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public void saveResource(String auth, ValueSets data, HttpHeaders headers) {	
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kik/definition_kik_v1_codes.json");
			ODPRequest<ValueSetType> req = mapper.readValue(is, ODPRequest.class);
			req.setRecords(data.getValueSet());
			adapterClient.createResource(auth, headers, req, ODPRequest.class);
		} catch (IOException e) {
			throw new ODPException(e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		try {
			ObjectMapper om = new ObjectMapper();
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(KIK_CODES_DEFINITION);
			ODPRequest<CodeType> req = om.readValue(is, ODPRequest.class);
			
			Codes c = new Codes();
			req.setRecords(c.getCode());
			req.getRecords().add(new CodeType());
			System.out.println(om.writeValueAsString(req));
		} catch (IOException e) {
			throw new ODPException(e.getClass().getName() + ":" + e.getMessage());
		}

	}
}
