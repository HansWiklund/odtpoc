package se.inera.odp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.odp.client.CKANClient;
import se.inera.odp.request.ODPRequest;

@Service
public class CKANService {

	@Autowired
	CKANClient ckanClient;

	@Autowired
	ObjectMapper mapper;

	@SuppressWarnings("unchecked")
	public String getDataById(String id) {
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
	
	public void saveData(String data) {	
		System.out.println(data);
		// ckanClient.createResource((String) data.getData());
	}

}
