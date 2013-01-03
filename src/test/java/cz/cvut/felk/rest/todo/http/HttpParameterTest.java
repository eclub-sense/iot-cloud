package cz.cvut.felk.rest.todo.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HttpParameterTest {

	@Test
	public void testReadNull() {
		assertNull(HttpParameter.read(new HttpScanner(null)));
	}
	
	@Test
	public void testReadToken() {
		HttpParameter param = HttpParameter.read(new HttpScanner("a=b"));
		assertNotNull(param);
		assertEquals("a", param.getAttribute());
		assertEquals("b", param.getValue());

		HttpParameter param2 = HttpParameter.read(new HttpScanner("1adf=234"));
		assertNotNull(param2);
		assertEquals("1adf", param2.getAttribute());
		assertEquals("234", param2.getValue());
	}

	@Test
	public void testReadString() {
		HttpParameter param = HttpParameter.read(new HttpScanner("a=\"b\""));
		assertNotNull(param);
		assertEquals("a", param.getAttribute());
		assertEquals("\"b\"", param.getValue());

		HttpParameter param2 = HttpParameter.read(new HttpScanner("1adf=\"234\""));
		assertNotNull(param2);
		assertEquals("1adf", param2.getAttribute());
		assertEquals("\"234\"", param2.getValue());
	}

	@Test
	public void testReadEscString() {
		HttpParameter param = HttpParameter.read(new HttpScanner("a=\"\\\"b\""));
		assertNotNull(param);
		assertEquals("a", param.getAttribute());
		assertEquals("\"\\\"b\"", param.getValue());

		HttpParameter param2 = HttpParameter.read(new HttpScanner("1adf=\"23\'4\""));
		assertNotNull(param2);
		assertEquals("1adf", param2.getAttribute());
		assertEquals("\"23\'4\"", param2.getValue());
	}
}
