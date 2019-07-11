package se.inera.odp.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyMap;
import java.io.IOException;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import se.inera.odp.controller.ODPController;
import se.inera.odp.core.exception.ODPAuthorizationException;

public class ODPControllerTest {
	
	@InjectMocks
	private ODPController odpController;
	
	@Mock
	private ODPService odpService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetResourceById() {
		String mockedResult = "[{\"testfield1\":\"test1\",\"testfield2\":\"test2\"},{\"testfield1\":\"test3\",\"testfield2\":\"test4\"}]";
		when(odpService.getResourceById(anyString(), anyString(), anyMap(), anyString())).thenReturn(mockedResult);
		
		ResponseEntity<String> response = odpController.getResourceById("5e90db9b-0e4a-4b14-b5f6-755dbffccadb", "test", "testresource", new HashMap<String, String>());
		
		assertNotNull(response);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), mockedResult);
	}

	@Test
	public void testCreateResource() {
		String mockedResult = "{\"success\":true}";
		when(odpService.createResource(anyString(), anyString())).thenReturn(mockedResult);
		
		ResponseEntity<String> response = odpController.createResource("5e90db9b-0e4a-4b14-b5f6-755dbffccadb", "");
		
		assertNotNull(response);
		assertEquals(response.getBody(), mockedResult);
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	}

	@Test
	public void testCreateResourceWithNullAuth() {
		try {
			ResponseEntity<String> response = odpController.createResource(null, "");
		}
		catch (ODPAuthorizationException e) {
			assertEquals(e.getStatus(), HttpStatus.FORBIDDEN);
		}
	}
	
	@Test
	public void testGetPingResponse() {
		ResponseEntity<String> response = odpController.getPingResponse();
		String responseBody = response.getBody();
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(responseBody, "OK");		
	}

	@Test
	public void testDeleteResource() throws IOException {
		String mockedResult = "{\"success\":true}";
		when(odpService.deleteResource(anyString(), anyString(), anyString())).thenReturn(mockedResult);
		
		ResponseEntity<String> response = odpController.deleteResource("5e90db9b-0e4a-4b14-b5f6-755dbffccadb", "test", "testresource");
		
		assertNotNull(response);
		assertEquals(response.getBody(), mockedResult);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void testUpdateResource() throws IOException {
		String mockedResult = "{\"success\":true}";
		when(odpService.updateResource(anyString(), anyString(), anyString(), anyString())).thenReturn(mockedResult);
		
		ResponseEntity<String> response = odpController.updateResource("5e90db9b-0e4a-4b14-b5f6-755dbffccadb", "test", "testresource", "");
	
		assertNotNull(response);
		assertEquals(response.getBody(), mockedResult);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

}
