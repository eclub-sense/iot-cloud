package cz.esc.iot.cloudservice.resources;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.support.ErrorJson;

public class Code extends ServerResource {

	@Get("json")
	public String code() {

		// get code from url parameters
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String code = form.getFirstValue("code");

		if (code == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			return gson.toJson(new ErrorJson("invalid_grant"));
		}
		return "{\"code\":\"" + code + "\"}";
	}
}
