package cz.cvut.felk.rest.todo.http;

import static org.junit.Assert.*;

import org.junit.Test;

public class HttpParameterTest {

	@Test
	public void testReadNull() {
		assertNull(HttpParameter.readParameter(new HttpScanner(null)));
	}
	
	@Test
	public void testRead() {
		
		HttpParameter param = HttpParameter.readParameter(new HttpScanner("a=b"));
		assertNotNull(param);
	}
	
}
