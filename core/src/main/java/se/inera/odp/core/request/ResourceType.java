package se.inera.odp.core.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 	"resource": {
		"package_id": "kvalitetsindikatorer",
		"name": "kik-v1-codes",
        "hash": "kik-v1-codes__"
	},
 * @author hanwik
 *
 */
public class ResourceType {

	@JsonProperty("package_id")
	String packageId;
	String name;
	String hash;
	
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	
}
