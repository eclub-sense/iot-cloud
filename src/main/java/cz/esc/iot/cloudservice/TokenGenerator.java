package cz.esc.iot.cloudservice;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.restlet.engine.util.Base64;


public class TokenGenerator {

	public static void main(String args[]) {
		for(int i = 0; i < 100; i++) {
			Random randomGenerator = new Random();
			int rand = randomGenerator.nextInt(Integer.MAX_VALUE);
			String random = String.valueOf(rand);
			
			String creationDate = new Date().toString();
			
			String keySource = "mapaclik@gmail.com" + creationDate + random;
			
			String token = Base64.encode(keySource.getBytes(), false);
			System.out.println(token);
		}
	}
}
