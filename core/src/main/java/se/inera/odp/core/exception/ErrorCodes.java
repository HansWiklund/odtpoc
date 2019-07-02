package se.inera.odp.core.exception;

public class ErrorCodes {

	// From old PFOD
	
	// The error code to print in the log if an error occurs in a BUH API channel.
	public static final String ERROR_CODE_BUH_API = "ODP027";
	// The error code to print if the incoming URL can not be parsed into an existing channel.
	public static final String ERROR_CODE_CHANNEL_NOT_FOUND = "ODP021";
	// The error code to print if a request to CKAN's datastore_create API fails.
	public static final String ERROR_CODE_CKAN_DATASTORE_CREATE = "ODP009";
	// The error code to print if a request to CKAN's datastore_delete API fails.
	public static final String ERROR_CODE_CKAN_DATASTORE_DELETE = "ODP008";
	// The error code to print if a request to CKAN's datastore_search API fails.
	public static final String ERROR_CODE_CKAN_DATASTORE_SEARCH = "ODP010";
	// The error code to print if a request to CKAN's datastore_update API fails.
	public static final String ERROR_CODE_CKAN_DATASTORE_UPDATE = "ODP012";
	// The error code to print if a request to CKAN's package_show API fails.
	public static final String ERROR_CODE_CKAN_PACKAGE_SHOW = "ODP026";
	// The error code to print if a request to CKAN's resource_delete API fails.
	public static final String ERROR_CODE_CKAN_RESOURCE_DELETE = "ODP007";
	// The error code to print if a CKAN resource is not found.
	public static final String ERROR_CODE_CKAN_RESOURCE_NOT_FOUND = "ODP013";
	// The error code to print if a request to CKAN's resource_search API fails.
	public static final String ERROR_CODE_CKAN_RESOURCE_SEARCH = "ODP006";
	// The error code to print if an unexpected error occurrs in one of the CKAN channels.
	public static final String ERROR_CODE_CKAN_UNEXPECTED = "ODP011";
	// The error code to print if the format of some data is not the expected.
	public static final String ERROR_CODE_FORMAT = "ODP014";
	// The error code to print if the setup fails in the global script.
	public static final String ERROR_CODE_GLOBAL_SCRIPT = "ODP025";
	// The error code to print in the log if an error occurs in a HSA API channel.
	public static final String ERROR_CODE_HSA_API = "ODP017";
	// The error code to print in the log if the HSA import fails.
	public static final String ERROR_CODE_HSA_IMPORT = "ODP002";
	// The error code to print in the log if an error occurs in a KIK API channel.
	public static final String ERROR_CODE_KIK_API = "ODP016";
	// The error code to print in the log if a KIK import fails.
	public static final String ERROR_CODE_KIK_IMPORT = "ODP001";
	// The error code to print in the log if an error occurs in an NKK API channel.
	public static final String ERROR_CODE_NKK_API = "ODP018";
	// The error code to print in the log if the NKK import fails.
	public static final String ERROR_CODE_NKK_IMPORT = "ODP003";
	// The error code to print in the log if a soap request fails during the NTJP import.
	public static final String ERROR_CODE_NTJP_IMPORT = "ODP004";
	// The error code to print in the log if the NTJP load from old platform fails.
	public static final String ERROR_CODE_NTJP_LOAD_FROM_OLD_PLATFORM = "ODP005";
	// The error code to print if an unexpected error is caught in the postAndRenew channel.
	public static final String ERROR_CODE_POST_AND_RENEW = "ODP024";
	// The error code to print if the creation of an attachment fails in the API Router channel.
	public static final String ERROR_CODE_ROUTER_ATTACHMENT_CREATE = "ODP020";
	// The error code to print if an unexpected error is caught in the API Router channel.
	public static final String ERROR_CODE_ROUTER_UNEXPECTED = "ODP022";
	// The error code to print if an  error occurs in a channel handling updates.
	public static final String ERROR_CODE_UPDATE = "ODP023";
	// The error code to print in the log if an error occurs in an Indikatorvarden API channel.
	public static final String ERROR_CODE_VALUES_API = "ODP019";
	// The error code to print if an XML to JSON parsing error occurs.
	public static final String ERROR_CODE_XML_TO_JSON_PARSING = "ODP015";

}
