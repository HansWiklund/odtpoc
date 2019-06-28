package se.inera.odp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.odp.client.CKANClient;
import se.inera.odp.exception.ODPException;
import se.inera.odp.request.CKANResponse;
import se.inera.odp.request.CKANResult;
import se.inera.odp.request.ODPResponse;

@Service
public class ODPService {

	@Autowired
	CKANClient ckanClient;

	@Autowired
	ObjectMapper mapper;

	@SuppressWarnings("unchecked")
	public String getResourceById(String dataset_id, String resource_id, Map<String, String> params) {
		ResponseEntity<String> result;
		
		try {
			result = ckanClient.getResource(dataset_id);
	
			// Extrakt id of result set
			Map<String, ?> resultMap = createResultAsMap(result.getBody());
			List<Map<String, String>> resourceList = (List<Map<String, String>>)resultMap.get("resources");
		
			Optional<Map<String, String>> resource = resourceList.stream().filter(r -> resource_id.equals(r.get("name"))).findFirst();
			
			String resourceId = null;
			if(resource.isPresent())
				resourceId = resource.get().get("id");
			else
				throw new ODPException("Resource " + resource_id + "does not exist");
	
			// Get result set
			ResponseEntity<CKANResponse> response = ckanClient.getData(resourceId, params, CKANResponse.class);
			
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
				odpResponse.setLinks(ckanResult.getLinks());
				odpResponse.setOffset(ckanResult.getOffset());
				odpResponse.setLimit(ckanResult.getLimit());
				odpResponse.setTotal(ckanResult.getTotal());
				return mapper.writeValueAsString(odpResponse);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, ?> createResultAsMap(String value) throws IOException {
		Map<String, Object> map = mapper.readValue(value, Map.class);
		return (Map<String, Object>)map.get("result");
	}
	
<<<<<<< HEAD
	@SuppressWarnings("unchecked")
	public void createData(String auth, String contentType, String data) throws IOException
=======
	public void createResource(String auth, String contentType, String data)
>>>>>>> 5db15badbc10555302129c8b2dafd26aaf8367df
	{
		// Kör en resource_search
		Map<String, Object> map = mapper.readValue(data, Map.class);
		Map<String, Object> innerMap = (Map<String, Object>)map.get("resource");
		String hashName = (String)innerMap.get("hash");
		ResponseEntity<String> result = ckanClient.getResourceForId(hashName);
		
		Map<String, Object> returnMap = mapper.readValue(result.getBody(), Map.class);
		Map<String, Object> resultInnerMap = (Map<String, Object>)returnMap.get("result");
		int count = (int)resultInnerMap.get("count");
		
		if (count > 0)
		{
			System.out.println("count > 0");
			// Plocka ut gammalt resourceId
			List<?> tmpList = (List<?>)resultInnerMap.get("results");
			int lastindex = tmpList.size()-1;
			Map<String, String> resultsMap = (Map<String, String>)tmpList.get(lastindex);
			String oldResourceId = resultsMap.get("id");
			
			// Lägg till ny resurs
			ckanClient.createResource(auth, contentType, data);
			
			// Ta bort gammal resurs
			ckanClient.deleteResource(auth, contentType, oldResourceId);
		}
		else 
		{
			System.out.println("count inte större än 0");
			// Lägg till ny resurs
			ckanClient.createResource(auth, contentType, data);
		}
	}
	
}
