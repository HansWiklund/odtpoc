package se.inera.odp.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import se.inera.odp.core.exception.ODPAuthorizationException;

@Service
public class CKANClient {

	// URL for creating a new datastore in CKAN
	@Value("${ckan.datastore.url}/action/datastore_create")
	private String CKAN_DATASTORE_CREATE_URL;
		
	// URL for deleting a resource from CKAN
	@Value("${ckan.datastore.url}/action/resource_delete")
	private String CKAN_RESOURCE_DELETE_URL;
	
	// URL for searching for a resource in CKAN
	@Value("${ckan.datastore.url}/action/resource_search?query=hash:")
	private String CKAN_RESOURCE_SEARCH_URL;
	
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
		return this.getData(id, params, String.class);
	}			
	

	public <T> ResponseEntity<T> getData(String id, Map<String, String> params, Class<T> clazz) {
		if(id == null)
			return null;
		
		if(params == null)
			params = new HashMap<String, String>();
		
		params.put("id", id);
		params.putIfAbsent("limit", CKAN_DATASTORE_SEARCH_LIMIT);
		
		UriComponentsBuilder _uri = UriComponentsBuilder.fromUriString(CKAN_DATASTORE_SEARCH_URL);
		for(String k : params.keySet())	{
			_uri.queryParam(k, params.get(k));
		}
		
		URI uri = _uri.build().toUri();

		return restTemplate.getForEntity(uri, clazz);
	}
	
	public ResponseEntity<String> getResourceForId(String hashName) {
		if(hashName == null)
			return null;
		return restTemplate.getForEntity(CKAN_RESOURCE_SEARCH_URL + hashName, String.class);	
	}

	public void createResource(String auth, String data) {
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", auth);
		headers.add("Content-Type", "application/json");
		
		HttpEntity<String> request = new HttpEntity<String>(data, headers);
		System.out.println(data);
		restTemplate.postForObject(CKAN_DATASTORE_CREATE_URL, request, HttpEntity.class);
	}
	
	// TODO:
	public void updateResource(String data) {
		restTemplate.postForEntity(CKAN_DATASTORE_UPDATE_URL, data, String.class);
	}

	// TODO:
	public void deleteResource(String auth, String id) {
		
		String resourceToDelete = "{\"id\":\"" + id + "\"}";
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", auth);
		headers.add("Content-Type", "application/json");
		
		HttpEntity<?> request = new HttpEntity<String>(resourceToDelete, headers);
		
		restTemplate.postForObject(CKAN_RESOURCE_DELETE_URL, request, String.class);
	}

}
