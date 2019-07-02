package se.inera.odp.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AdapterClient {

	// URL for creating a new datastore in CKAN
	@Value("${server.datastore.url}/save")
	private String SERVER_CREATE_URL;
				
		
	@Autowired
	RestTemplate restTemplate;

	// Send data to ckan
	public <T> void createResource(HttpHeaders headers, T data, Class clazz) {
		
		
		MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<String, String>();
		headersMap.add("Authorization", "abc123");
		headersMap.add("Content-Type", "application/json");
		
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<T> request = new HttpEntity<T>(data, headersMap);
		restTemplate.postForEntity(SERVER_CREATE_URL, request, clazz);
	}
	
	// TODO: Get data from producer
	public ResponseEntity<String> getResource(String id) {
		if(id == null)
			return null;
		return restTemplate.getForEntity("", String.class, id);
	}	
}
