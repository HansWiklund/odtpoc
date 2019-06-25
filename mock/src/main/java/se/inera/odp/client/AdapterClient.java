package se.inera.odp.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdapterClient {

	// URL for creating a new datastore in CKAN
	@Value("${server.datastore.url}/save")
	private String SERVER_CREATE_URL;
				
		
	@Autowired
	RestTemplate restTemplate;

	public <T> void createResource(T data, Class clazz) {
		restTemplate.postForEntity(SERVER_CREATE_URL, data, clazz);
	}

	
}
