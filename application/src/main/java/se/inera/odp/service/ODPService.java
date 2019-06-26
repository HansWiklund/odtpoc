package se.inera.odp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.odp.client.CKANClient;
import se.inera.odp.exception.ODPException;

@Service
public class ODPService {

	@Autowired
	CKANClient ckanClient;

	@Autowired
	ObjectMapper mapper;

	@SuppressWarnings("unchecked")
	public String getResourceById(String dataset_id, String resource_id) {
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
			result = ckanClient.getData(resourceId);

			// Remove _id
			resultMap = createResultAsMap(result.getBody());
			List<?> resultList = (List<?>)resultMap.get("records");
			for(Object record : resultList) {
				((Map<String, String>)record).remove("_id");
			}
			List<Map> fieldList = (List<Map>)resultMap.get("fields");
			for(Map<String, String>field : fieldList) {
				
				if("_id".equals(field.get("id"))) {
					fieldList.remove(field);
					break;
				}
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
	
	public void createResource(String auth, String contentType, String data)
	{
		ckanClient.createResource(auth, contentType, data);
	}
	
}
