package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.restlet.ext.oauth.OAuthException;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Swagger extends ServerResource {

	@Get
    public String returnList() throws OAuthException {
		
		String path = this.getRequest().getResourceRef().getPath();
		String fileName = null;
		
		if (path.equals("/api"))
			fileName = "/home/z3tt0r/swagger/index.html";
		else
			fileName = "/home/z3tt0r/swagger"+path;
			
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
