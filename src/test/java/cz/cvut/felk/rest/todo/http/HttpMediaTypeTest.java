package cz.cvut.felk.rest.todo.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HttpMediaTypeTest {

	@Test
	public void testReadNull() {
		assertNull(HttpMediaType.read(new HttpScanner(null)));
	}
	
	@Test
	public void testRead() {
		HttpMediaType t = HttpMediaType.read(new HttpScanner("application/json")); 
		assertNotNull(t);
		assertEquals("application", t.getType());
		assertEquals("json", t.getSubtype());
		assertNull(t.getParameters());
		
		HttpMediaType t2 = HttpMediaType.read(new HttpScanner("x/vnd.a.b.c+xml")); 
		assertNotNull(t2);
		assertEquals("x", t2.getType());
		assertEquals("vnd.a.b.c+xml", t2.getSubtype());
		assertNull(t2.getParameters());
	}
	
	@Test
	public void testReadWithParams() {
		HttpMediaType t = HttpMediaType.read(new HttpScanner("text/html;a=b;1234=\"\\\"5\"")); 
		assertNotNull(t);
		assertEquals("text", t.getType());
		assertEquals("html", t.getSubtype());
		
		assertNotNull(t.getParameters());
		assertEquals(2, t.getParameters().length);
		
		assertNotNull(t.getParameters()[0]);
		assertEquals("a", t.getParameters()[0].getAttribute());
		assertEquals("b", t.getParameters()[0].getValue());
		
		assertNotNull(t.getParameters()[1]);
		assertEquals("1234", t.getParameters()[1].getAttribute());
		assertEquals("\"\\\"5\"", t.getParameters()[1].getValue());
	}	
}
