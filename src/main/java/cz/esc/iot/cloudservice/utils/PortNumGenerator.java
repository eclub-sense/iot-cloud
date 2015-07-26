package cz.esc.iot.cloudservice.utils;

import java.util.Random;

/**
 * 
 */
public class PortNumGenerator {
	private static boolean inUse[] = new boolean[1000];
	
	public static int freePort() {
		Random randomGenerator = new Random();
		int random;
		while (inUse[random = randomGenerator.nextInt(1000)]);
		return random + 9000;
	}
}
