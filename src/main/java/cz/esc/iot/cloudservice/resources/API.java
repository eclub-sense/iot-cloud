package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.restlet.ext.oauth.OAuthException;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class API extends ServerResource {

	@Get("json")
    public String returnList() throws OAuthException {		

		String path = this.getRequest().getResourceRef().getPath();
		
		if (path.equals("/api.json")) {
	        String fileName = "API.json";
	        try {
	            return new String(Files.readAllBytes(Paths.get(fileName)));
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
		} else {
	        String fileName = "/home/z3tt0r/swagger/index.html";
	        try {
	            return new String(Files.readAllBytes(Paths.get(fileName)));
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
		}
    }
}
