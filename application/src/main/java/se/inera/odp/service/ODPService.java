package se.inera.odp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.odp.client.CKANClient;
import se.inera.odp.core.exception.ODPException;
import se.inera.odp.core.request.CKANError;
import se.inera.odp.core.request.CKANResponse;
import se.inera.odp.core.request.CKANResult;
import se.inera.odp.core.request.LinkType;
import se.inera.odp.core.request.ODPResponse;

import static se.inera.odp.core.exception.ErrorCodes.*;

@Service
public class ODPService {

	// Supported ckan query parameters
	private static final ArrayList<String> qparams = new ArrayList<>(
			Arrays.asList(
					"limit", 
					"offset", 
					"fields", 
					"sort"
				));
	
	// Not supported ckan query parameters
	private static final ArrayList<String> uparams = new ArrayList<>(
			Arrays.asList(	
				"resource_id",
				"filters",
				"q",
				"distinct", 
				"plain",
				"language", 
				"include_total", 
				"records_format"
			));
	
	@Autowired
	CKANClient ckanClient;

//	@Autowired
	ObjectMapper mapper;
	
	@Value("${app.server.url}")
	String serverUrl;
	
	@Autowired
	public void setMapper(ObjectMapper mapper){
		this.mapper = mapper;
	}


	public String getResourceById(String dataset_id, String resource_name, Map<String, String> params, String auth) {
		
		String resourceId = fetchResourceId(auth, dataset_id, resource_name);

		// Get result set
		computeQuery(params);
		ResponseEntity<CKANResponse> response = null;
		try {
			response = ckanClient.getData(auth, resourceId, params, CKANResponse.class);
		} catch (RuntimeException e) {
			throw new ODPException(createStatus(response), e.getMessage(), ERROR_CODE_CKAN_DATASTORE_SEARCH);			
		}
		
		CKANResponse ckanResponse = response.getBody();

		if(!ckanResponse.getSuccess()) {
			throw new ODPException(HttpStatus.NOT_FOUND, ckanResponse.getError().getMessage(), ERROR_CODE_CKAN_DATASTORE_SEARCH);
		}
		
		CKANResult ckanResult = ckanResponse.getResult();
		List<Map<String, ?>> ckanRecords = ckanResult.getRecords();
		
		for(Map<String, ?> rec : ckanRecords) {
			rec.remove("_id");
		}
		
		try {		
			if(ckanResult.getTotal() == ckanRecords.size()) {
				return mapper.writeValueAsString(ckanRecords);				
			} else {			
				ODPResponse odpResponse = new ODPResponse();
				odpResponse.setRecords(ckanRecords);
				reformatLinks(ckanResult.getLinks(), dataset_id, resource_name);
				odpResponse.setLinks(ckanResult.getLinks());
				odpResponse.setOffset(ckanResult.getOffset());
				odpResponse.setLimit(ckanResult.getLimit());
				odpResponse.setTotal(ckanResult.getTotal());
				return mapper.writeValueAsString(odpResponse);
			}						
		} catch (IOException e) {
			throw new ODPException("IOException : " + e.getMessage());
		}
	}
	
	private void reformatLinks(LinkType lt, String dataset, String resource) {
		String next = lt.getNext();
		String prev = lt.getPrev();
		
		String nextLimit = "";
		String nextOffset = "";
		
		String prevLimit = "";
		String prevOffset = "";
		
		if (next != null) {
			int startIndex = next.indexOf("limit=") + 6;
			int endIndex = next.indexOf('&');
			nextLimit = next.substring(startIndex, endIndex);
			startIndex = next.indexOf("offset=") + 7;
			endIndex = next.length();
			nextOffset = next.substring(startIndex, endIndex);
		}
		
		if (prev != null) {
			int startIndex = prev.indexOf("limit=") + 6;
			int endIndex = prev.indexOf('&');
			prevLimit = prev.substring(startIndex, endIndex);
			startIndex = prev.indexOf("offset=") + 7;
			endIndex = prev.length();
			prevOffset = prev.substring(startIndex, endIndex);
		}
		
		lt.setStart(serverUrl + "/api/get/" + dataset + "/" + resource);
		if (next != null)
			lt.setNext(serverUrl + "/api/get/" + dataset + "/" + resource + "?limit=" + nextLimit + "&offset=" + nextOffset);
		else 
			lt.setNext(null);
		if (prev != null)
			lt.setPrev(serverUrl + "/api/get/" + dataset + "/" + resource + "?limit=" + prevLimit + "&offset=" + prevOffset);
		else
			lt.setPrev(null);
	}

	@SuppressWarnings("unchecked")
	public String createResource(String auth, String data) {
		try {
			// Kör en resource_search
			Map<String, Object> map = mapper.readValue(data, Map.class);
			Map<String, Object> innerMap = (Map<String, Object>)map.get("resource");
			String hashName = (String)innerMap.get("hash");
			ResponseEntity<String> result = null;
			try {
				result = ckanClient.getResourceForId(auth, hashName);
			} catch (RuntimeException e) {
				throw new ODPException(createStatus(result), e.getMessage(), ERROR_CODE_CKAN_RESOURCE_SEARCH);			
			}			
			Map<String, Object> returnMap = mapper.readValue(result.getBody(), Map.class);
			Map<String, Object> resultInnerMap = (Map<String, Object>)returnMap.get("result");
			int count = (int)resultInnerMap.get("count");
			
			if (count > 0)
			{
				// Plocka ut gammalt resourceId
				List<?> tmpList = (List<?>)resultInnerMap.get("results");
				int lastindex = tmpList.size()-1;
				Map<String, String> resultsMap = (Map<String, String>)tmpList.get(lastindex);
				String oldResourceId = resultsMap.get("id");
				
				// Lägg till ny resurs
				ResponseEntity<String> response = null;
				try {
					response = ckanClient.createResource(auth, data);
				} catch (RuntimeException e) {
					throw new ODPException(createStatus(response), e.getMessage(), ERROR_CODE_CKAN_DATASTORE_CREATE);			
				}			

				Map<String, ?> resultMap = mapper.readValue(response.getBody(), Map.class);
				resultMap.remove("help");
				resultMap.remove("result");
			
				// Ta bort gammal resurs
				try {
					response = ckanClient.deleteResource(auth, oldResourceId);
				} catch (RuntimeException e) {
					throw new ODPException(createStatus(response), e.getMessage(), ERROR_CODE_CKAN_DATASTORE_DELETE);			
				}	
			
				return mapper.writeValueAsString(resultMap);
			}
			else 
			{
				// Lägg till ny resurs
				ResponseEntity<String> response = null;
				try {
					response = ckanClient.createResource(auth, data);
				} catch (RuntimeException e) {
					throw new ODPException(createStatus(response), e.getMessage(), ERROR_CODE_CKAN_DATASTORE_CREATE);			
				}			
				Map<String, ?> resultMap = mapper.readValue(response.getBody(), Map.class);
				resultMap.remove("help");
				resultMap.remove("result");
				return mapper.writeValueAsString(resultMap);
			}
		} catch(IOException e) {
			throw new ODPException("IOException : " + e.getMessage());			
		}
	}
	
	public String deleteResource(String auth, String dataset_id, String resource_name) throws IOException {
		String resourceId = fetchResourceId(auth, dataset_id, resource_name);
		ResponseEntity<String> response = null;
		try {
			response = ckanClient.deleteResource(auth, resourceId);
		} catch (RuntimeException e) {
			throw new ODPException(createStatus(response), e.getMessage(), ERROR_CODE_CKAN_DATASTORE_DELETE);			
		}
		
		@SuppressWarnings("unchecked")
		Map<String, ?> map = mapper.readValue(response.getBody(), Map.class);
		map.remove("result");
		map.remove("help");
		return mapper.writeValueAsString(map);
	}

	public String updateResource(String auth, String dataset_id, String resource_name, String data) throws JsonParseException, JsonMappingException, IOException {
		
		String resourceId = fetchResourceId(auth, dataset_id, resource_name);
			
		StringBuilder strBldr = new StringBuilder();
		strBldr.append("{\"resource_id\": \"" + resourceId + "\", \"force\": true, ");
		int recordsIndex = data.indexOf("\"records\"");
		int lastIndexOfCurlyBrace = data.lastIndexOf("}");
		strBldr.append(data.substring(recordsIndex, lastIndexOfCurlyBrace));
		strBldr.append(", \"method\" : \"upsert\" }");
		
		ResponseEntity<String> response = null;
		try {
			response = ckanClient.updateResource(auth, strBldr.toString());
		} catch (RuntimeException e) {
			throw new ODPException(createStatus(response), e.getMessage(), ERROR_CODE_CKAN_DATASTORE_UPDATE);			
		}			
		
		@SuppressWarnings("unchecked")
		Map<String, ?> map = mapper.readValue(response.getBody(), Map.class);
		map.remove("help");
		map.remove("method");
		map.remove("resource_id");
		map.remove("result");
		return mapper.writeValueAsString(map);
	}

	/*
	 * Private methods
	 */
	
	private void computeQuery(Map<String, String> params) {
		try {
			Map<String, String> filters = new HashMap<>();
			
			Iterator<Map.Entry<String,String>> iter = params.entrySet().iterator();
			while (iter.hasNext()) {
			    Map.Entry<String,String> entry = iter.next();
				if(uparams.contains(entry.getKey())) {
			        iter.remove();		    
				} else if(!qparams.contains(entry.getKey())) {
					filters.put(entry.getKey(), entry.getValue());
			        iter.remove();
				}
			}
			
			if(filters.size()>0)
				params.put("filters", mapper.writeValueAsString(filters));
			
		} catch (IOException e) {
			throw new ODPException("IOException : " + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, ?> createResultAsMap(String value) {
		try {
			Map<String, Object> map = mapper.readValue(value, Map.class);
			
			Boolean success = (Boolean) map.get("success");
			if(!success) {
				CKANError error = new CKANError(map);
				throw new ODPException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), ERROR_CODE_CKAN_PACKAGE_SHOW);
			}
			return (Map<String, Object>)map.get("result");
			
		} catch(IOException e) {
			throw new ODPException("IOException : " + e.getMessage());			
		}			
	}

	@SuppressWarnings("unchecked")
	private String fetchResourceId(String auth, String dataset_id, String resource_name) {

		ResponseEntity<String> result = null;
		try {
			result = ckanClient.getResource(auth, dataset_id, String.class);
		} catch (HttpClientErrorException e) {			
			throw new ODPException(e.getStatusCode(), e.getMessage(), ERROR_CODE_CKAN_PACKAGE_SHOW);					
		} catch (RuntimeException e) {
			throw new ODPException(createStatus(result), e.getMessage(), ERROR_CODE_CKAN_PACKAGE_SHOW);			
		}
		Map<String, ?> resultMap = createResultAsMap(result.getBody());
		List<Map<String, String>> resourceList = (List<Map<String, String>>)resultMap.get("resources");		
		Optional<Map<String, String>> resource = resourceList.stream().filter(r -> resource_name.equals(r.get("name"))).findFirst();
	
		if(!resource.isPresent())
			throw new ODPException(HttpStatus.NOT_FOUND, "Resursen " + resource_name + " saknas!", ERROR_CODE_CKAN_PACKAGE_SHOW);
		
		Map<String, String> resourceMap = resource.get();
		
		return resourceMap.get("id");
			
	}
	
	private HttpStatus createStatus(ResponseEntity<?> response) {
		
		if(response == null)
			return HttpStatus.INTERNAL_SERVER_ERROR;
		
		if(response.getStatusCode() == null)
			return HttpStatus.INTERNAL_SERVER_ERROR;
		
		return response.getStatusCode();
	}
}
