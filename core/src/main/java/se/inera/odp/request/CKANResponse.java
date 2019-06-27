package se.inera.odp.request;

public class CKANResponse {

	String help;
	Boolean success;
	CKANResult result;
	
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
	
}
