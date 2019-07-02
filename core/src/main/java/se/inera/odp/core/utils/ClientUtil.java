package se.inera.odp.core.utils;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ClientUtil {

	public static MultiValueMap<String, String> createHeaders(String auth) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		if(auth != null && auth.length() > 0)
			headers.add("Authorization", auth);
		headers.add("Content-Type", "application/json");
		
		return headers;
	}
}
