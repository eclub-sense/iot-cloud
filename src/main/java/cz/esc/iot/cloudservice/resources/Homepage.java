package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONException;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.oauth.AccessTokenServerResource;
import org.restlet.ext.oauth.GrantType;
import org.restlet.ext.oauth.OAuthParameters;
import org.restlet.ext.oauth.OAuthProxy;
import org.restlet.ext.oauth.internal.Token;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public class Homepage extends AccessTokenServerResource {

	
    @Get("html")
    public String returnList() throws IOException, JSONException {

    	Form form = getRequest().getResourceRef().getQueryAsForm();
    	String code = form.getFirstValue("code");
    	String state = form.getFirstValue("state");
    	String redirect_uri = "http://localhost:8081/callback";
    	String grant_type = "authorization_code";
    	String f = "{";
    	f += "code:" + code;
    	f += ",state:" + state;
    	f += ",redirect_uri:" + redirect_uri;
    	//f += ",grant_type:" + grant_type;
    	f += ",client_id:" + clientID;
    	f += ",client_secret:" + clientSecret +"}";
    	System.out.println(f);
    	Representation rep = new JsonRepresentation(f);
    	System.out.println(rep.getText());
    	
    	ClientResource client = new ClientResource("http://localhost:8089/callback");
    	Representation res = client.post(rep);
    	System.out.println(res);
    	

    	/*Form form = getRequest().getResourceRef().getQueryAsForm();
    	AccessTokenClientResource client = new AccessTokenClientResource(new Reference("https://accounts.google.com/o/oauth2/token"));
    	client.setClientCredentials(clientID, clientSecret);
    	OAuthParameters params = new OAuthParameters();
    	params.code(form.getFirstValue("code"));
    	params.state(form.getFirstValue("state"));
    System.out.println(form.getFirstValue("code"));
    System.out.println(form.getFirstValue("state"));
    	params.redirectURI("http://localhost:8081/callback");
    	params.grantType(GrantType.authorization_code);
    	Token token = client.requestToken(params);
    	System.out.println("Homepage");
    	System.out.println(token);*/
    	
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
    
    @Post("json")
    public String p(Representation entity) throws IOException {
    	System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa");
    	return entity.getText();
    }
}
