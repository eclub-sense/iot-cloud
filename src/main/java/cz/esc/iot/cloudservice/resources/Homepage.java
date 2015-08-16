package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.restlet.ext.oauth.AccessTokenServerResource;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.resource.Get;

import cz.esc.iot.cloudservice.oauth2.GoogleUserInfo;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

public class Homepage extends AccessTokenServerResource {

	@Get("html")
    public String returnList() throws OAuthException {
		
		// Identify user
		GoogleUserInfo googleUser = null;
		try {
			googleUser = OAuth2.getUserMail(getRequest());
			System.out.println("Google user's mail: " + googleUser.getEmail());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		/*
		// Find user in db
		UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("identifier").equal(googleUser.getEmail()).get();
		// if user is not in db, register him
		if (userEntity == null)
			UserRegistrator.registerUser(googleUser.getEmail());
		*/
		
        String fileName = "index.html";
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
