package cz.esc.iot.cloudservice.oauth2;

import com.google.gson.annotations.Expose;

public class CloudToken {
	
    @Expose private String access_token;
    @Expose private final String token_type = "bearer";
    @Expose private final int expires_in = 3600;
    @Expose private String refresh_token;
    
	public CloudToken(String accessToken, String refreshToken) {
		this.access_token = accessToken;
		this.refresh_token = refreshToken;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
}
