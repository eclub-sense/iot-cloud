package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.restlet.ext.oauth.OAuthException;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Homepage extends ServerResource {
	
	@Get("html")
    public String returnList() throws OAuthException {		

        String fileName = "haha.html";
        StringBuilder sb = new StringBuilder();

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName),
                    Charset.defaultCharset());
            for (String line : lines) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
