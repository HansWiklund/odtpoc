package se.inera.odp.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.odp.client.CKANClient;
import se.inera.odp.core.request.CKANResponse;
import se.inera.odp.core.request.CKANResult;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ODPServiceTest {

	@InjectMocks
	private ODPService odpService;
	
	@Mock
	private CKANClient ckanClient;
	
	@Autowired
	private ObjectMapper mapper;

	@Before
	public void setUp() throws Exception {
		mapper = new ObjectMapper();
		odpService.setMapper(mapper);
		MockitoAnnotations.initMocks(this);
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetResourceById() throws IOException {

		String resourceString = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=package_show\", \"success\": true, \"result\": {\"license_title\": \"Creative Commons Attribution\", \"maintainer\": \"\", \"relationships_as_object\": [], \"private\": false, \"maintainer_email\": \"\", \"num_tags\": 0, \"id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"metadata_created\": \"2019-06-19T13:42:41.145901\", \"metadata_modified\": \"2019-08-13T09:44:40.086222\", \"author\": \"\", \"author_email\": \"\", \"state\": \"active\", \"version\": \"\", \"creator_user_id\": \"5e9ffcea-0da6-4236-8456-4345138bd8de\", \"type\": \"dataset\", \"resources\": [{\"mimetype\": null, \"cache_url\": null, \"hash\": \"testresource__\", \"description\": \"\", \"name\": \"testresource\", \"format\": \"\", \"url\": \"http://localhost:5000/datastore/dump/701e888a-d0c7-4308-82dc-2d841e8c603e\", \"datastore_active\": true, \"cache_last_updated\": null, \"package_id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"created\": \"2019-07-12T10:54:18.951981\", \"state\": \"active\", \"mimetype_inner\": null, \"last_modified\": null, \"position\": 0, \"revision_id\": \"513545a0-9cad-45d7-b4ac-530c0d3051fa\", \"url_type\": \"datastore\", \"id\": \"701e888a-d0c7-4308-82dc-2d841e8c603e\", \"resource_type\": null, \"size\": null}, {\"mimetype\": null, \"cache_url\": null, \"hash\": \"testingLinda2__\", \"description\": \"\", \"name\": \"testingLinda2\", \"format\": \"\", \"url\": \"http://localhost:5000/datastore/dump/2f019578-1175-461f-bf75-d3c0be0dae95\", \"datastore_active\": true, \"cache_last_updated\": null, \"package_id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"created\": \"2019-08-13T09:44:39.599106\", \"state\": \"active\", \"mimetype_inner\": null, \"last_modified\": null, \"position\": 1, \"revision_id\": \"119888a6-4052-4be7-9661-1cd75e64be47\", \"url_type\": \"datastore\", \"id\": \"2f019578-1175-461f-bf75-d3c0be0dae95\", \"resource_type\": null, \"size\": null}], \"num_resources\": 2, \"tags\": [], \"groups\": [], \"license_id\": \"cc-by\", \"relationships_as_subject\": [], \"organization\": null, \"name\": \"test\", \"isopen\": true, \"url\": \"\", \"notes\": \"lala\", \"owner_org\": null, \"extras\": [], \"license_url\": \"http://www.opendefinition.org/licenses/cc-by\", \"title\": \"test\", \"revision_id\": \"9db78899-e8ac-47fe-9815-e652607cb527\"}}";
	
		ResponseEntity<String> resultEntity = new ResponseEntity<>(resourceString, HttpStatus.OK);
		
		when(ckanClient.getResource(anyString(), anyString(), eq(String.class))).thenReturn(resultEntity);
		
		String mockedGetDataString = "{\"testfield1\":\"test1\",\"testfield2\":\"test2\"}";
		Map<String, ?> map = mapper.readValue(mockedGetDataString, Map.class);
				
		List<Map<String, ?>> ckanRecords = new ArrayList<>();
		ckanRecords.add(map);
		
		CKANResult ckanResult = new CKANResult(); 
		ckanResult.setRecords(ckanRecords);
		ckanResult.setTotal(ckanRecords.size());
		
		CKANResponse ckanResponse = new CKANResponse();
		ckanResponse.setResult(ckanResult);
		ckanResponse.setSuccess(true);
		
		ResponseEntity<CKANResponse> responseEntity = new ResponseEntity<>(ckanResponse, HttpStatus.OK);
		
		when(ckanClient.getData(any(), any(), anyMap(), eq(CKANResponse.class))).thenReturn(responseEntity);
		
		String response = odpService.getResourceById("test", "testresource", new HashMap<String, String>(), "5e90db9b-0e4a-4b14-b5f6-755dbffccadb");
		
		assertNotNull(response);
		assertEquals(response, "[" + mockedGetDataString + "]");
	}

	@Test
	public void testCreateResource() {
		
		String getResourceForId = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=resource_search\", \"success\": true, \"result\": {\"count\": 1, \"results\": [{\"mimetype\": null, \"cache_url\": null, \"state\": \"active\", \"hash\": \"testingLinda2__\", \"description\": \"\", \"format\": \"\", \"url\": \"http://localhost:5000/datastore/dump/46a230e1-ad17-4c02-871b-d3af50fde3d0\", \"datastore_active\": true, \"created\": \"2019-08-13T12:25:39.976788\", \"cache_last_updated\": null, \"package_id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"mimetype_inner\": null, \"last_modified\": null, \"position\": 1, \"revision_id\": \"ca3c61ba-4581-4847-bb9c-d2d4a79111a4\", \"size\": null, \"url_type\": \"datastore\", \"id\": \"46a230e1-ad17-4c02-871b-d3af50fde3d0\", \"resource_type\": null, \"name\": \"testingLinda2\"}]}}";
		String createResource = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=datastore_create\", \"success\": true, \"result\": {\"resource\": {\"url\": \"_datastore_only_resource\", \"mimetype\": null, \"package_id\": \"test\", \"hash\": \"testingLinda2__\", \"name\": \"testingLinda2\"}, \"primary_key\": \"testfield1\", \"resource_id\": \"a0fb2bf5-dc35-40e8-8ccf-d2ff73c57dd4\", \"fields\": [{\"type\": \"text\", \"id\": \"testfield1\"}, {\"type\": \"text\", \"id\": \"testfield2\"}], \"method\": \"insert\"}}";
		String deleteResource = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=resource_delete\", \"success\": true, \"result\": null}";
	
		ResponseEntity<String> responseEntityGetResourceForId = new ResponseEntity<>(getResourceForId, HttpStatus.OK);
		ResponseEntity<String> responseEntityCreateResource = new ResponseEntity<>(createResource, HttpStatus.OK);
		ResponseEntity<String> responseEntityDeleteResource = new ResponseEntity<>(deleteResource, HttpStatus.OK);
		
		when(ckanClient.getResourceForId(any(), any())).thenReturn(responseEntityGetResourceForId);
		when(ckanClient.createResource(any(), any())).thenReturn(responseEntityCreateResource);
		when(ckanClient.deleteResource(any(), any())).thenReturn(responseEntityDeleteResource);
		
		String data = "{\n" + 
				"	\"resource\": {\n" + 
				"		\"package_id\": \"test\",\n" + 
				"		\"name\": \"testingLinda2\",\n" + 
				"   		\"hash\": \"testingLinda2__\"\n" + 
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
		
		String correctResponse = "{\"success\":true}";
		
		String response = odpService.createResource("5e90db9b-0e4a-4b14-b5f6-755dbffccadb", data);
		
		assertNotNull(response);
		assertEquals(response, correctResponse);
	}

	@Test
	public void testDeleteResource() throws IOException {
		String resourceString = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=package_show\", \"success\": true, \"result\": {\"license_title\": \"Creative Commons Attribution\", \"maintainer\": \"\", \"relationships_as_object\": [], \"private\": false, \"maintainer_email\": \"\", \"num_tags\": 0, \"id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"metadata_created\": \"2019-06-19T13:42:41.145901\", \"metadata_modified\": \"2019-08-13T09:44:40.086222\", \"author\": \"\", \"author_email\": \"\", \"state\": \"active\", \"version\": \"\", \"creator_user_id\": \"5e9ffcea-0da6-4236-8456-4345138bd8de\", \"type\": \"dataset\", \"resources\": [{\"mimetype\": null, \"cache_url\": null, \"hash\": \"testresource__\", \"description\": \"\", \"name\": \"testresource\", \"format\": \"\", \"url\": \"http://localhost:5000/datastore/dump/701e888a-d0c7-4308-82dc-2d841e8c603e\", \"datastore_active\": true, \"cache_last_updated\": null, \"package_id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"created\": \"2019-07-12T10:54:18.951981\", \"state\": \"active\", \"mimetype_inner\": null, \"last_modified\": null, \"position\": 0, \"revision_id\": \"513545a0-9cad-45d7-b4ac-530c0d3051fa\", \"url_type\": \"datastore\", \"id\": \"701e888a-d0c7-4308-82dc-2d841e8c603e\", \"resource_type\": null, \"size\": null}, {\"mimetype\": null, \"cache_url\": null, \"hash\": \"testingLinda2__\", \"description\": \"\", \"name\": \"testingLinda2\", \"format\": \"\", \"url\": \"http://localhost:5000/datastore/dump/2f019578-1175-461f-bf75-d3c0be0dae95\", \"datastore_active\": true, \"cache_last_updated\": null, \"package_id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"created\": \"2019-08-13T09:44:39.599106\", \"state\": \"active\", \"mimetype_inner\": null, \"last_modified\": null, \"position\": 1, \"revision_id\": \"119888a6-4052-4be7-9661-1cd75e64be47\", \"url_type\": \"datastore\", \"id\": \"2f019578-1175-461f-bf75-d3c0be0dae95\", \"resource_type\": null, \"size\": null}], \"num_resources\": 2, \"tags\": [], \"groups\": [], \"license_id\": \"cc-by\", \"relationships_as_subject\": [], \"organization\": null, \"name\": \"test\", \"isopen\": true, \"url\": \"\", \"notes\": \"lala\", \"owner_org\": null, \"extras\": [], \"license_url\": \"http://www.opendefinition.org/licenses/cc-by\", \"title\": \"test\", \"revision_id\": \"9db78899-e8ac-47fe-9815-e652607cb527\"}}";
		ResponseEntity<String> resultEntity = new ResponseEntity<>(resourceString, HttpStatus.OK);
		when(ckanClient.getResource(anyString(), anyString(), eq(String.class))).thenReturn(resultEntity);
		
		String deleteResource = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=resource_delete\", \"success\": true, \"result\": null}";
		ResponseEntity<String> responseEntityDeleteResource = new ResponseEntity<>(deleteResource, HttpStatus.OK);
		when(ckanClient.deleteResource(any(), any())).thenReturn(responseEntityDeleteResource);

		String response = odpService.deleteResource("5e90db9b-0e4a-4b14-b5f6-755dbffccadb", "test", "testingLinda2");
		
		String correctResponse = "{\"success\":true}";
		
		assertNotNull(response);
		assertEquals(response, correctResponse);
	}

	@Test
	public void testUpdateResource() throws IOException {
		String resourceString = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=package_show\", \"success\": true, \"result\": {\"license_title\": \"Creative Commons Attribution\", \"maintainer\": \"\", \"relationships_as_object\": [], \"private\": false, \"maintainer_email\": \"\", \"num_tags\": 0, \"id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"metadata_created\": \"2019-06-19T13:42:41.145901\", \"metadata_modified\": \"2019-08-13T09:44:40.086222\", \"author\": \"\", \"author_email\": \"\", \"state\": \"active\", \"version\": \"\", \"creator_user_id\": \"5e9ffcea-0da6-4236-8456-4345138bd8de\", \"type\": \"dataset\", \"resources\": [{\"mimetype\": null, \"cache_url\": null, \"hash\": \"testresource__\", \"description\": \"\", \"name\": \"testresource\", \"format\": \"\", \"url\": \"http://localhost:5000/datastore/dump/701e888a-d0c7-4308-82dc-2d841e8c603e\", \"datastore_active\": true, \"cache_last_updated\": null, \"package_id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"created\": \"2019-07-12T10:54:18.951981\", \"state\": \"active\", \"mimetype_inner\": null, \"last_modified\": null, \"position\": 0, \"revision_id\": \"513545a0-9cad-45d7-b4ac-530c0d3051fa\", \"url_type\": \"datastore\", \"id\": \"701e888a-d0c7-4308-82dc-2d841e8c603e\", \"resource_type\": null, \"size\": null}, {\"mimetype\": null, \"cache_url\": null, \"hash\": \"testingLinda2__\", \"description\": \"\", \"name\": \"testingLinda2\", \"format\": \"\", \"url\": \"http://localhost:5000/datastore/dump/2f019578-1175-461f-bf75-d3c0be0dae95\", \"datastore_active\": true, \"cache_last_updated\": null, \"package_id\": \"696209af-af22-40b3-924d-94aeb63751ab\", \"created\": \"2019-08-13T09:44:39.599106\", \"state\": \"active\", \"mimetype_inner\": null, \"last_modified\": null, \"position\": 1, \"revision_id\": \"119888a6-4052-4be7-9661-1cd75e64be47\", \"url_type\": \"datastore\", \"id\": \"2f019578-1175-461f-bf75-d3c0be0dae95\", \"resource_type\": null, \"size\": null}], \"num_resources\": 2, \"tags\": [], \"groups\": [], \"license_id\": \"cc-by\", \"relationships_as_subject\": [], \"organization\": null, \"name\": \"test\", \"isopen\": true, \"url\": \"\", \"notes\": \"lala\", \"owner_org\": null, \"extras\": [], \"license_url\": \"http://www.opendefinition.org/licenses/cc-by\", \"title\": \"test\", \"revision_id\": \"9db78899-e8ac-47fe-9815-e652607cb527\"}}";
		ResponseEntity<String> resultEntity = new ResponseEntity<>(resourceString, HttpStatus.OK);
		when(ckanClient.getResource(anyString(), anyString(), eq(String.class))).thenReturn(resultEntity);
		
		String updateResource = "{\"help\": \"http://localhost:5000/api/3/action/help_show?name=datastore_upsert\", \"success\": true, \"result\": {\"records\": [{\"testfield2\": \"blabla\", \"testfield1\": \"test1\"}], \"method\": \"upsert\", \"resource_id\": \"a0fb2bf5-dc35-40e8-8ccf-d2ff73c57dd4\"}}";
		ResponseEntity<String> resultEntityUpdateResource = new ResponseEntity<>(updateResource, HttpStatus.OK);
		when(ckanClient.updateResource(anyString(), anyString())).thenReturn(resultEntityUpdateResource);
		
		String data = "{ \n" + 
				"  \"records\": [ \n" + 
				"  	{ \"testfield1\":\"test1\", \"testfield2\": \"blabla\" }\n" + 
				"   ]\n" + 
				"}";
		
		String response = odpService.updateResource("5e90db9b-0e4a-4b14-b5f6-755dbffccadb", "test", "testingLinda2", data);
		
		String correctResponse = "{\"success\":true}";
		
		assertNotNull(response);
		assertEquals(response, correctResponse);
	}

}

