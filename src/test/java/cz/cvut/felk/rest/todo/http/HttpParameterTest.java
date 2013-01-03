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
		assertEquals("a", param.getAttribute());
		assertEquals("b", param.getValue());

		HttpParameter param2 = HttpParameter.readParameter(new HttpScanner("1adf=234"));
		assertNotNull(param2);
		assertEquals("1adf", param2.getAttribute());
		assertEquals("234", param2.getValue());
	}
	
}
