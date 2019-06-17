package se.inera.odp.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CKANClient {

	// URL for creating a new datastore in CKAN
	@Value("${ckan.datastore.create.url}")
	private String CKAN_DATASTORE_CREATE_URL;
		
	// URL for deleting a resource from CKAN
	@Value("${ckan.resource.delete.url}")
	private String CKAN_RESOURCE_DELETE_URL;
			
	// The maximum number of posts to retrieve from a CKAN resource in one single request.
	@Value("${ckan.datastore.search.limit}")
	private String CKAN_DATASTORE_SEARCH_LIMIT;
	
	// URL for searching CKAN's datastore
	@Value("${ckan.datastore.search.url}")
	private String CKAN_DATASTORE_SEARCH_URL;
		
	// URL for updating a datastore in CKAN
	@Value("${ckan.datastore.update.url}")
	private String CKAN_DATASTORE_UPDATE_URL;
		
	// URL used for displaying files in a certain dataset in CKAN
	@Value("${ckan.datastore.url}/action/package_show?id=")
	private String CKAN_PACKAGE_SHOW_URL;
		
		
	@Autowired
	RestTemplate restTemplate;

	// TODO
	public ResponseEntity<String> getResource(String id) {
		if(id == null)
			return null;
		return restTemplate.getForEntity(CKAN_PACKAGE_SHOW_URL + id, String.class);
	}

	public ResponseEntity<String> getData(String id) {
		if(id == null)
			return null;
		return restTemplate.getForEntity(CKAN_DATASTORE_SEARCH_URL + "?id=" + id, String.class);
	}

	// TODO:
	public void createResource(String data) {
		restTemplate.postForEntity(CKAN_DATASTORE_CREATE_URL, data, String.class).getBody();
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
