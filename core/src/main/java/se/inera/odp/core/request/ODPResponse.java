package se.inera.odp.core.request;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ODPResponse {
	
	private Integer total;
	private Integer limit;
	private Integer offset;
	private List<Map<String, ?>> records;
	private LinkType links;
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public List<Map<String, ?>> getRecords() {
		return records;
	}
	public void setRecords(List<Map<String, ?>> records) {
		this.records = records;
	}
	public LinkType getLinks() {
		return links;
	}
	public void setLinks(LinkType links) {
		this.links = links;
	}
	
}
