package cz.esc.iot.cloudservice.resources;

import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Code extends ServerResource {

	@Get("json")
	public String code() {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String code = form.getFirstValue("code");
		if (code == null)
			return "{\n\"error\":\"Authorisation code required.\",\n\"code\":1\n}";
		return "{\n\"code\":\"" + code + "\"\n}";
	}
}
