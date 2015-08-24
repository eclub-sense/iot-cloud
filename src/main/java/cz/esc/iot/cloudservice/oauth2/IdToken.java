package cz.esc.iot.cloudservice.oauth2;

public class IdToken {

	private String iss;
	private String aud;
	private String iat;
	private String exp;
	private String user_id;
	private String email;
	private String provider_id;
	private String verified;
	private String photo_url;
	
	public String getIss() {
		return iss;
	}
	public String getAud() {
		return aud;
	}
	public String getIat() {
		return iat;
	}
	public String getExp() {
		return exp;
	}
	public String getUser_id() {
		return user_id;
	}
	public String getEmail() {
		return email;
	}
	public String getProvider_id() {
		return provider_id;
	}
	public String getVerified() {
		return verified;
	}
	public String getPhoto_url() {
		return photo_url;
	}
}
