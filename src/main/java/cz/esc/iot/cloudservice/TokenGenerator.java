package cz.esc.iot.cloudservice;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.restlet.engine.util.Base64;


public class TokenGenerator {

	public static void main(String args[]) {
		for(int i = 0; i < 100; i++) {
			Random random = new Random();
			int len = 40;

	        byte[] token = new byte[len];
	        random.nextBytes(token);
	        String strToken = String.valueOf(Hex.encodeHex(token));

			System.out.println(strToken);
		}
	}
}
