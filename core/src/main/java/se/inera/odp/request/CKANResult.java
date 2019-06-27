package se.inera.odp.request;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CKANResult {

	@JsonProperty("include_total")
	Boolean includeTotal;
	
	@JsonProperty("resource_id")
	String resourceId;
	
	private Set<FieldType> fields = new HashSet<>();

	@JsonProperty("records_format")
	private String recordFormats;
	
	private List<Map<String, ?>> records;
	
	Integer limit;
	
	Integer total;
	
	@JsonProperty("_links")
	LinkType links;

	public Boolean getIncludeTotal() {
		return includeTotal;
	}

	public void setIncludeTotal(Boolean includeTotal) {
		this.includeTotal = includeTotal;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Set<FieldType> getFields() {
		return fields;
	}

	public void setFields(Set<FieldType> fields) {
		this.fields = fields;
	}

	public String getRecordFormats() {
		return recordFormats;
	}

	public void setRecordFormats(String recordFormats) {
		this.recordFormats = recordFormats;
	}

	public List<Map<String, ?>> getRecords() {
		return records;
	}

	public void setRecords(List<Map<String, ?>> records) {
		this.records = records;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public LinkType getLinks() {
		return links;
	}

	public void setLinks(LinkType links) {
		this.links = links;
	}
	
	
	
}
