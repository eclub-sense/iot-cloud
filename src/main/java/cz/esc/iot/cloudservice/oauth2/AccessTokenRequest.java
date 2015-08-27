package cz.esc.iot.cloudservice.oauth2;

import com.google.gson.annotations.Expose;

public class AccessTokenRequest {

	@Expose private String grant_type;
	@Expose private String code;
	@Expose private String client_id;
	
	public AccessTokenRequest() {
		super();
	}
	
	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
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
