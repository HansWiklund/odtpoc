package se.inera.odp.core.request;

import java.util.Map;


/*
 *  {
 *    "help": "Creates a package",
 *   "success": false,
 *   "error": {
 *       "message": "Access denied",
 *       "__type": "Authorization Error"
 *       }
 * }
 */
 
public class CKANError {
	String message;
	String __type;

	public CKANError() {}

	public CKANError( Map<String, Object> map ) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> error = (Map<String, Object>)map.get("error");
		
		if(error != null) {
			this.setMessage((String) error.get("message"));
			this.set__type((String) error.get("__type"));
		}
}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String get__type() {
		return __type;
	}
	public void set__type(String __type) {
		this.__type = __type;
	}
	
	
}
