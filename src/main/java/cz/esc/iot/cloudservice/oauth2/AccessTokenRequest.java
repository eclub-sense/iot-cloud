package cz.esc.iot.cloudservice.oauth2;

public class AccessTokenRequest {

	private String grant_type;
	private String code;
	private String client_id;
	
	public AccessTokenRequest() {
		
	}
	public String getGrant_type() {
		return grant_type;
	}
	public String getCode() {
		return code;
	}
	public String getClient_id() {
		return client_id;
	}
}
