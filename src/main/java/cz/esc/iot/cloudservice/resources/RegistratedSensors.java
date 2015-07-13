package cz.esc.iot.cloudservice.resources;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import cz.esc.iot.cloudservice.RestletApplication;

public class RegistratedSensors extends ServerResource {
	
	@Get("json")
	public JsonRepresentation sensors() {
		return ((RestletApplication)this.getApplication()).registry.toJson();
	}
}
