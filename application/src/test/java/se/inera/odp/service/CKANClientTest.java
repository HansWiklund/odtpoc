package se.inera.odp.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import se.inera.odp.client.CKANClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CKANClientTest {

	@InjectMocks
	private CKANClient ckanClient;
	
	@Mock
    private RestTemplate restTemplate;
	
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
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetResource() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetData() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetResourceForId() {

		String responseString = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=resource_search\", \"success\": true, \"result\": {\"count\": 1, \"results\": [{\"mimetype\": null, \"cache_url\": null, \"state\": \"active\", \"hash\": \"testingLinda2__\", \"description\": \"\", \"format\": \"\", \"url\": \"http://localhost:5000/datastore/dump/46a230e1-ad17-4c02-871b-d3af50fde3d0\", \"datastore_active\": true, \"created\": \"2019-08-13T12:25:39.976788\", \"cache_last_updated\": null, \"package_id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"mimetype_inner\": null, \"last_modified\": null, \"position\": 1, \"revision_id\": \"ca3c61ba-4581-4847-bb9c-d2d4a79111a4\", \"size\": null, \"url_type\": \"datastore\", \"id\": \"46a230e1-ad17-4c02-871b-d3af50fde3d0\", \"resource_type\": null, \"name\": \"testingLinda2\"}]}}";
		ResponseEntity<String> result = new ResponseEntity<String>(responseString, HttpStatus.OK);
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class))).thenReturn(result);
		
		ResponseEntity<String> response = ckanClient.getResourceForId("5e90db9b-0e4a-4b14-b5f6-755dbffccadb", "testingLinda2__");
		 
		assertNotNull(response);
		assertEquals(responseString, response.getBody());
	}
/*
	@Test
	public void testCreateResource() {
		String responseString = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=datastore_create\", \"success\": true, \"result\": {\"resource\": {\"url\": \"_datastore_only_resource\", \"mimetype\": null, \"package_id\": \"test\", \"hash\": \"testingLinda4__\", \"name\": \"testingLinda4\"}, \"primary_key\": \"testfield1\", \"resource_id\": \"d4f399c0-3a2a-49da-a050-d82592ca4955\", \"fields\": [{\"type\": \"text\", \"id\": \"testfield1\"}, {\"type\": \"text\", \"id\": \"testfield2\"}], \"method\": \"insert\"}}";
		ResponseEntity<String> result = new ResponseEntity<String>(responseString, HttpStatus.OK);
		when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class))).thenReturn(result);

		String data = "{\n" + 
				"	\"resource\": {\n" + 
				"		\"package_id\": \"test\",\n" + 
				"		\"name\": \"testingLinda4\",\n" + 
				"   		\"hash\": \"testingLinda4__\"\n" + 
				"	},\n" + 
				"	\"fields\": [{\n" + 
				"		\"id\": \"testfield1\",\n" + 
				"		\"type\": \"text\"\n" + 
				"	},\n" + 
				"	{\n" + 
				"		\"id\": \"testfield2\",\n" + 
				"		\"type\": \"text\"\n" + 
				"	}\n" + 
				"	],\n" + 
				"	\"primary_key\": \"testfield1\",\n" + 
				"	\"records\": [\n" + 
				"{\n" + 
				"\"testfield1\": \"test1\",\n" + 
				"\"testfield2\": \"test2\"\n" + 
				"}\n" + 
				"]\n" + 
				"}";
		
		ResponseEntity<String> response = ckanClient.createResource("5e90db9b-0e4a-4b14-b5f6-755dbffccadb", data);
		
		assertNotNull(response); 
		assertEquals(responseString, response.getBody());
	}
*/
	@Test
	public void testUpdateResourceString() {
//		fail("Not yet implemented");
	}

	@Test
	public void testDeleteResource() {
//		fail("Not yet implemented");
	}

	@Test
	public void testUpdateResourceStringString() {
//		fail("Not yet implemented");
	}

}