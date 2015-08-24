package cz.esc.iot.cloudservice.oauth2;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

public class Checker {

    private final List mClientIDs;
    private final String mAudience;
    private final GoogleIdTokenVerifier mVerifier;
    private final JsonFactory mJFactory;
    //private final GooglePublicKeysManager keyManager;
    private String mProblem = "Verification failed. (Time-out?)";

    public Checker(String[] clientIDs, String audience) {
        mClientIDs = Arrays.asList(clientIDs);
        mAudience = audience;
        NetHttpTransport transport = new NetHttpTransport();
        mJFactory = new GsonFactory();
        mVerifier = new GoogleIdTokenVerifier(transport, mJFactory);
        //keyManager = new GooglePublicKeysManager(transport, mJFactory);
    }

    public GoogleIdToken.Payload check(String tokenString) {
    	/*try {
			keyManager.refresh();
		} catch (GeneralSecurityException | IOException e1) {
			e1.printStackTrace();
		}*/
    	GoogleIdToken.Payload payload = null;
        System.out.println("a");
        try {
            GoogleIdToken token = GoogleIdToken.parse(mJFactory, tokenString);
            mVerifier.getPublicKeysManager().refresh();
            System.out.println(mVerifier.verify(token.toString()));
            System.out.println("b: " + token);
            if (mVerifier.verify(token)) {
                GoogleIdToken.Payload tempPayload = token.getPayload();
                System.out.println("c: " + tempPayload);
                if (!tempPayload.getAudience().equals(mAudience)) {
                    mProblem = "Audience mismatch";
                	System.out.println("d");
                } else if (!mClientIDs.contains(tempPayload.getAuthorizedParty())) {
                    mProblem = "Client ID mismatch";
                } else
                    payload = tempPayload;
            }
        } catch (GeneralSecurityException e) {
            mProblem = "Security issue: " + e.getLocalizedMessage();
        } catch (IOException e) {
            mProblem = "Network problem: " + e.getLocalizedMessage();
        }
        return payload;
    }

    public String problem() {
        return mProblem;
    }
}