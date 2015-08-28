/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 *
 * @author alf
 */
public class Css extends ServerResource {

	@Get
    public String returnList() throws OAuthException {		

        String fileName = "css/style.css";
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
