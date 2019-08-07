package se.inera.odp.core.request;

public class CKANResponse {

	private String help;
	private Boolean success;
	private CKANResult result;
	private CKANError error;
	
	public String getHelp() {
		return help;
	}
	public void setHelp(String help) {
		this.help = help;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public CKANResult getResult() {
		return result;
	}
	public void setResult(CKANResult result) {
		this.result = result;
	}
	public CKANError getError() {
		return error;
	}
	public void setError(CKANError error) {
		this.error = error;
	}
}
