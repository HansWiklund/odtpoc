package se.inera.odp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.odp.client.CKANClient;

@Service
public class CKANService {

	@Autowired
	CKANClient ckanClient;
	
	public ResponseEntity<String> getDataById(String id) {
		ResponseEntity<String> result = ckanClient.getResource(id);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = null;
		try {
			map = mapper.readValue(result.getBody(), Map.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Extrakt id of result set
		Map<String, Object> resultMap = (Map<String, Object>)map.get("result");
		List<?> resourceList = (List<?>)resultMap.get("resources");
		int i = resourceList.size()-1;
		Map r = (Map)resourceList.get(i);
		String uuid = (String)r.get("id");

		// return data;
		return ckanClient.getData(uuid);
	}

}
