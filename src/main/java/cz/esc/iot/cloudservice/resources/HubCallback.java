package cz.esc.iot.cloudservice.resources;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class HubCallback extends ServerResource {

	@Get
	public void redirect() {

		// get code and state from url parameters
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String code = form.getFirstValue("code");
		String state = form.getFirstValue("state");

		if (code == null || state == null)
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		
		// redirection to uri defined in state parameter
		getResponse().redirectPermanent(state + "?code=" + code);
	}
}
