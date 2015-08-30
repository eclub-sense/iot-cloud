package cz.esc.iot.cloudservice.support;

import com.google.gson.annotations.Expose;

public class ErrorJson {

	@Expose private String error;
	
	public ErrorJson() {
		super();
	}
	
	public ErrorJson(String e) {
		error = e;
	}
}
