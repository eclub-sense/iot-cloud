package cz.esc.iot.cloudservice.resources;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.hubs.Hub;
import cz.esc.iot.cloudservice.registry.ConnectedHubRegistry;

public class RegisteredHubs extends ServerResource {

	@Get("json")
	public String returnList() {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String path = this.getRequest().getResourceRef().getPath();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		switch (path) {
		case "/registered_hubs" : return registeredHubs(gson, form);
		default : return gson.toJson(ConnectedHubRegistry.getInstance().get(Integer.parseInt((String)this.getRequestAttributes().get("uuid"))));
		}
	}
	private String registeredHubs(Gson gson, Form form) {
		ConnectedHubRegistry result = new ConnectedHubRegistry();
		String status = null;
		for (Parameter parameter : form) {
			if (parameter.getName().equals("status")) {
				status = parameter.getValue();
			}
		}
		if (status != null) {
			for (Hub hub : ConnectedHubRegistry.getInstance().getList()) {
				if (hub.getStatus().equals(status)) {
					result.add(hub);
				}
			}
			result.setTotalCount(ConnectedHubRegistry.getInstance().getList().size());
			return gson.toJson(result);
		} else {
			return gson.toJson(ConnectedHubRegistry.getInstance());
		}
		
	}
}
