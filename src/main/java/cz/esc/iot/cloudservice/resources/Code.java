package cz.esc.iot.cloudservice.resources;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Code extends ServerResource {

	@Get("json")
	public String code() {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String code = form.getFirstValue("code");
		if (code == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return "{\n\"error\":\"invalid_grant\"\n}";
		}
		return "{\n\"code\":\"" + code + "\"\n}";
	}
}
