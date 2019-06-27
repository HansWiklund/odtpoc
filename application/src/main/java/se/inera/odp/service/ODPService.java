package se.inera.odp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.odp.client.CKANClient;

@Service
public class ODPService {

	@Autowired
	CKANClient ckanClient;

	@Autowired
	ObjectMapper mapper;

	@SuppressWarnings("unchecked")
	public String getResourceById(String id) {
		ResponseEntity<String> result;
		
		try {
			result = ckanClient.getResource(id);
	
			// Extrakt id of result set
			Map<String, ?> resultMap = createResultAsMap(result.getBody());
			List<?> resourceList = (List<?>)resultMap.get("resources");
			int lastindex = resourceList.size()-1;
			// TODO Handle error if lastindex = -1
			Map<String, String> resource = (Map<String, String>)resourceList.get(lastindex);
			String resourceId = resource.get("id");
	
			// Get result set
			result = ckanClient.getData(resourceId);
	
			resultMap = createResultAsMap(result.getBody());
			resourceList = (List<?>)resultMap.get("records");
			for(Object record : resourceList) {
				((Map<String, String>)record).remove("_id");
			}
	
			// return data;
			return mapper.writeValueAsString(resultMap);
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
	
	@SuppressWarnings("unchecked")
	public void createData(String auth, String contentType, String data) throws IOException
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
