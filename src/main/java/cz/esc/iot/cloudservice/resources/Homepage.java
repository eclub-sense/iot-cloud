package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.restlet.ext.oauth.OAuthException;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Homepage extends ServerResource {
	
	@Get("html")
    public String returnList() throws OAuthException {		

        String fileName = "g2.html";
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
