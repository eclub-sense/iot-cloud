package cz.esc.iot.cloudservice.resources;

import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.support.ErrorJson;

public class Code extends ServerResource {

	@Get("json")
	public String code() {

		System.out.println(getRequest().getRootRef().getFragment());
		System.out.println(getRequest().getReferrerRef().getFragment());
		System.out.println(getRequest().getResourceRef().getFragment());
		String code = null;
		if (code == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			return gson.toJson(new ErrorJson("invalid_grant"));
		}
		return "{\"code\":\"" + code + "\"}";
	}
}
