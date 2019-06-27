package se.inera.odp.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CKANClient {

	// URL for creating a new datastore in CKAN
	@Value("${ckan.datastore.url}/action/datastore_create")
	private String CKAN_DATASTORE_CREATE_URL;
		
	// URL for deleting a resource from CKAN
	@Value("${ckan.datastore.url}/action/resource_delete?id=")
	private String CKAN_RESOURCE_DELETE_URL;
			
	// The maximum number of posts to retrieve from a CKAN resource in one single request.
	@Value("${ckan.datastore.search.limit}")
	private String CKAN_DATASTORE_SEARCH_LIMIT;
	
	// URL for searching CKAN's datastore
	@Value("${ckan.datastore.url}/action/datastore_search")
	private String CKAN_DATASTORE_SEARCH_URL;
		
	// URL for updating a datastore in CKAN
	@Value("${ckan.datastore.url}/action/datastore_upsert")
	private String CKAN_DATASTORE_UPDATE_URL;
		
	// URL used for displaying files in a certain dataset in CKAN
	@Value("${ckan.datastore.url}/action/package_show?id={id}&limit={limit}")
	private String CKAN_PACKAGE_SHOW_URL;
		
		
	@Autowired
	RestTemplate restTemplate;

	// TODO
	public ResponseEntity<String> getResource(String id) {
		if(id == null)
			return null;
		return restTemplate.getForEntity(CKAN_PACKAGE_SHOW_URL, String.class, id, CKAN_DATASTORE_SEARCH_LIMIT);
	}

	public ResponseEntity<String> getData(String id, Map<String, String> params) {
		if(id == null)
			return null;
		
		if(params == null)
			params = new HashMap<String, String>();
		
		params.put("id", id);
		params.putIfAbsent("limit", CKAN_DATASTORE_SEARCH_LIMIT);
						
		URI uri = UriComponentsBuilder.fromUriString(CKAN_DATASTORE_SEARCH_URL)
		        .build()
		        .toUri();
		
		for(String k : params.keySet())	{
			uri = UriComponentsBuilder
			        .fromUri(uri)
			        .queryParam(k, params.get(k))
			        .build()
			        .toUri();
			}

		return restTemplate.getForEntity(uri, String.class);
	}

	public <T> ResponseEntity<T> getData(String id, Map<String, String> params, Class<T> clazz) {
		if(id == null)
			return null;
		
		if(params == null)
			params = new HashMap<String, String>();
		
		params.put("id", id);
		params.putIfAbsent("limit", CKAN_DATASTORE_SEARCH_LIMIT);
						
		URI uri = UriComponentsBuilder.fromUriString(CKAN_DATASTORE_SEARCH_URL)
		        .build()
		        .toUri();
		System.out.println(uri);

		for(String k : params.keySet())	{
			uri = UriComponentsBuilder
			        .fromUri(uri)
			        .queryParam(k, params.get(k))
			        .build()
			        .toUri();
			}
		System.out.println(uri);
		return restTemplate.getForEntity(uri, clazz);
	}

	// TODO:
	public void createResource(String auth, String contentType, String data) {
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", auth);
		headers.add("Content-Type", contentType);
		
		HttpEntity<?> request = new HttpEntity<String>(data, headers);
		
		restTemplate.postForObject(CKAN_DATASTORE_CREATE_URL, request, String.class);
	}
	
	// TODO:
	public void updateResource(String data) {
		restTemplate.postForEntity(CKAN_DATASTORE_UPDATE_URL, data, String.class);
	}

	// TODO:
	public void deleteResource(String data) {
		restTemplate.delete(CKAN_RESOURCE_DELETE_URL);
	}

 
	
}
