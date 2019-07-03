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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.odp.client.CKANClient;
import se.inera.odp.core.exception.ODPException;
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

	@SuppressWarnings("unchecked")
	public String getResourceById(String dataset_id, String resource_id, Map<String, String> params, String auth) {
		ResponseEntity<String> result;
		
		try {
			result = ckanClient.getResource(auth, dataset_id);
	
			// Extrakt id of result set
			Map<String, ?> resultMap = createResultAsMap(result.getBody());
			List<Map<String, String>> resourceList = (List<Map<String, String>>)resultMap.get("resources");
		
			Optional<Map<String, String>> resource = resourceList.stream().filter(r -> resource_id.equals(r.get("name"))).findFirst();
			
			String resourceId = null;
			if(resource.isPresent())
				resourceId = resource.get().get("id");
			else
				throw new ODPException("Resource " + resource_id + " does not exist");
	
			// Get result set
			computeQuery(params);
			ResponseEntity<CKANResponse> response = ckanClient.getData(auth, resourceId, params, CKANResponse.class);
			
			CKANResponse ckanResponse = response.getBody();
			CKANResult ckanResult = ckanResponse.getResult();
			
			List<Map<String, ?>> ckanRecords = ckanResult.getRecords();
			
			for(Map<String, ?> rec : ckanRecords) {
				rec.remove("_id");
			}
			
		if(ckanResult.getTotal() == ckanRecords.size()) {
				return mapper.writeValueAsString(ckanRecords);				
			} else {			
				ODPResponse odpResponse = new ODPResponse();
				odpResponse.setRecords(ckanRecords);
				reformatLinks(ckanResult.getLinks(), dataset_id, resource_id);
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
		
		// Hårdkodad url. Skall sättas i application.properties innan produktion.
		String url = "localhost:8085";
		
		lt.setStart(url + "/api/get/" + dataset + "/" + resource);
		if (next != null)
			lt.setNext(url + "/api/get/" + dataset + "/" + resource + "?limit=" + nextLimit + "&offset=" + nextOffset);
		else 
			lt.setNext(null);
		if (prev != null)
			lt.setPrev(url + "/api/get/" + dataset + "/" + resource + "?limit=" + prevLimit + "&offset=" + prevOffset);
		else
			lt.setPrev(null);
	}

	@SuppressWarnings("unchecked")
	private Map<String, ?> createResultAsMap(String value) throws IOException {
		Map<String, Object> map = mapper.readValue(value, Map.class);
		return (Map<String, Object>)map.get("result");
	}
	

	@SuppressWarnings("unchecked")
	public void createResource(String auth, String data) {
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
				ckanClient.createResource(auth, data);
				
				// Ta bort gammal resurs
				ckanClient.deleteResource(auth, oldResourceId);
			}
			else 
			{
				// Lägg till ny resurs
				ckanClient.createResource(auth, data);
			}
		} catch(IOException e) {
			throw new ODPException("IOException : " + e.getMessage());			
		}
	}
	
	private void computeQuery(Map<String, String> params) throws IOException {
		
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
	}
	
	@SuppressWarnings("unchecked")
	public boolean deleteResource(String auth, String dataset_id, String resource_id) throws IOException {
		ResponseEntity<String> result = ckanClient.getResource(auth, dataset_id);
		Map<String, ?> resultMap = createResultAsMap(result.getBody());
		List<Map<String, String>> resourceList = (List<Map<String, String>>)resultMap.get("resources");
		Optional<Map<String, String>> resource = resourceList.stream().filter(r -> resource_id.equals(r.get("name"))).findFirst();
		Map<String, String> resourceMap = resource.get();
		String resourceName =  resourceMap.get("name");
		if (resourceName.equals(resource_id)) {
			ckanClient.deleteResource(auth, resourceMap.get("id"));
			return true;
		}			
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean updateResource(String auth, String dataset_id, String resource_id, String data) throws IOException {
		ResponseEntity<String> result = ckanClient.getResource(auth, dataset_id);
		Map<String, ?> resultMap = createResultAsMap(result.getBody());				
		List<Map<String, String>> resourceList = (List<Map<String, String>>)resultMap.get("resources");		
		Optional<Map<String, String>> resource = resourceList.stream().filter(r -> resource_id.equals(r.get("name"))).findFirst();
		Map<String, String> resourceMap = resource.get();
		String resourceName =  resourceMap.get("name");		
		if (resourceName.equals(resource_id)) {			
			StringBuilder strBldr = new StringBuilder();
			strBldr.append("{\"resource_id\": \"" + resourceMap.get("id") + "\", \"force\": true, ");
			int recordsIndex = data.indexOf("\"records\"");
			int lastIndexOfCurlyBrace = data.lastIndexOf("}");
			strBldr.append(data.substring(recordsIndex, lastIndexOfCurlyBrace));
			strBldr.append(", \"method\" : \"upsert\" }");
			ckanClient.updateResource(auth, strBldr.toString());
			return true;
		}			
		return false;
	}
	
}
