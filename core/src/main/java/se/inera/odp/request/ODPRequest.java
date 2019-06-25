package se.inera.odp.request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "resource", "fields", "primary_key", "always_strings", "records"})
public class ODPRequest <T> {

	private Set<FieldType> fields = new HashSet<>();
	private ResourceType resource;
	private List<T> records;
	@JsonProperty("primary_key")
	private String primaryKey;
	@JsonProperty("always_strings")
	private List<String> alwaysStrings = new ArrayList<>();
	
	public ODPRequest() {};

	public ResourceType getResource() {
		return resource;
	}

	public void setResource(ResourceType resource) {
		this.resource = resource;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Set<FieldType> getFields() {
		return fields;
	}

	public void setFields(Set<FieldType> fields) {
		this.fields = fields;
	}

	public List<String> getAlwaysStrings() {
		return alwaysStrings;
	}

	public void setAlwaysStrings(List<String> alwaysStrings) {
		this.alwaysStrings = alwaysStrings;
	}
	
}
