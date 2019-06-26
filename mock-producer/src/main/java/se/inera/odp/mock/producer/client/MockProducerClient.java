package se.inera.odp.mock.producer.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MockProducerClient {

	// URL for creating a new datastore in CKAN
	@Value("${server.datastore.url}/save")
	private String SERVER_CREATE_URL;
				
		
	@Autowired
	RestTemplate restTemplate;

	// Send data to ckan
	public <T> void createResource(T data, Class clazz) {
		restTemplate.postForEntity(SERVER_CREATE_URL, data, clazz);
	}

	// TODO: Get data from producer
	public ResponseEntity<String> getResource(String id) {
		if(id == null)
			return null;
		return restTemplate.getForEntity("", String.class, id);
	}	
}
