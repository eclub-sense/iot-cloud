package cz.esc.iot.cloudservice.oauth2;

import com.google.gson.annotations.Expose;

public class AccessTokenRequest {

	@Expose private String grant_type;
	@Expose private String code;
	@Expose private String client_id;
	
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
