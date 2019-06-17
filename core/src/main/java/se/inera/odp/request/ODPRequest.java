package se.inera.odp.request;

public class ODPRequest {

	private String id;
	private String data;
	
	public ODPRequest() {};
	
	public ODPRequest(String id, String data) {
		super();
		this.id = id;
		this.data = data;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
