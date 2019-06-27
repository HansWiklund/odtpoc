package se.inera.odp.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
	@Value("${ckan.datastore.url}/action/datastore_search?id={id}&limit={limit}")
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

	public ResponseEntity<String> getData(String id) {
		if(id == null)
			return null;
		return restTemplate.getForEntity(CKAN_DATASTORE_SEARCH_URL, String.class, id, CKAN_DATASTORE_SEARCH_LIMIT);
	}
	
	public ResponseEntity<String> getResourceForId(String hashName) {
		if(hashName == null)
			return null;
		return restTemplate.getForEntity(CKAN_RESOURCE_SEARCH_URL + hashName, String.class);	
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
	public void deleteResource(String auth, String contentType, String id) {
		
		String resourceToDelete = "{\"id\":\"" + id + "\"}";
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", auth);
		headers.add("Content-Type", contentType);
		
		HttpEntity<?> request = new HttpEntity<String>(resourceToDelete, headers);
		
		restTemplate.postForObject(CKAN_RESOURCE_DELETE_URL, request, String.class);
	}

}
