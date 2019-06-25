package se.inera.odp.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import opendata.followup.groupoutcomes.qualitymeasures._2.CodeType;
import opendata.followup.groupoutcomes.qualitymeasures._2.Codes;
import se.inera.odp.client.AdapterClient;
import se.inera.odp.request.ODPRequest;

@Service
public class MockAdapterService {

	@Autowired
	AdapterClient adapterClient;

	@Autowired
	ObjectMapper mapper;

	private static final String KIK_CODES_DEFINITION = "kik/definition_kik_v1_codes.json";
	
	public void saveDataById(Codes data) {	
		
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kik/definition_kik_v1_codes.json");
			ODPRequest<CodeType> req = mapper.readValue(is, ODPRequest.class);
			req.setRecords(data.getCode());
			adapterClient.createResource(req,ODPRequest.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
