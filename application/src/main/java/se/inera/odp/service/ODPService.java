package se.inera.odp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.odp.client.CKANClient;
import se.inera.odp.core.exception.ODPException;
import se.inera.odp.core.exception.ODPNotFoundException;
import se.inera.odp.core.request.CKANResponse;
import se.inera.odp.core.request.CKANResult;
import se.inera.odp.core.request.LinkType;
import se.inera.odp.core.request.ODPResponse;

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

	@Autowired
	ObjectMapper mapper;
	
	@Value("${app.server.url]")
	String serverUrl;

	public String getResourceById(String dataset_id, String resource_name, Map<String, String> params, String auth) {
		
		String resourceId = fetchResourceId(auth, dataset_id, resource_name);

		// Get result set
		computeQuery(params);
		ResponseEntity<CKANResponse> response = ckanClient.getData(auth, resourceId, params, CKANResponse.class);
		
		CKANResponse ckanResponse = response.getBody();
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
			ResponseEntity<String> result = ckanClient.getResourceForId(auth, hashName);
			
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
				ResponseEntity<String> response = ckanClient.createResource(auth, data);
				Map<String, ?> resultMap = mapper.readValue(response.getBody(), Map.class);
				resultMap.remove("help");
				resultMap.remove("result");
			
				// Ta bort gammal resurs
				ckanClient.deleteResource(auth, oldResourceId);
				return mapper.writeValueAsString(resultMap);
			}
			else 
			{
				// Lägg till ny resurs
				ResponseEntity<String> response = ckanClient.createResource(auth, data);
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
		ResponseEntity<String> response = ckanClient.deleteResource(auth, resourceId);
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
		ResponseEntity<String> response = ckanClient.updateResource(auth, strBldr.toString());
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
	private Map<String, ?> createResultAsMap(String value) throws IOException {
		Map<String, Object> map = mapper.readValue(value, Map.class);
		return (Map<String, Object>)map.get("result");
	}

	@SuppressWarnings("unchecked")
	private String fetchResourceId(String auth, String dataset_id, String resource_name) {

		try {
			ResponseEntity<String> result = ckanClient.getResource(auth, dataset_id);
			Map<String, ?> resultMap = createResultAsMap(result.getBody());
			List<Map<String, String>> resourceList = (List<Map<String, String>>)resultMap.get("resources");		
			Optional<Map<String, String>> resource = resourceList.stream().filter(r -> resource_name.equals(r.get("name"))).findFirst();
	
			if(!resource.isPresent())
				throw new ODPNotFoundException("Resursen " + resource_name + " saknas!");
			
			Map<String, String> resourceMap = resource.get();
			
			return resourceMap.get("id");
			
		} catch(IOException e) {
			throw new ODPException("IOException : " + e.getMessage());			
		}
	}
}
