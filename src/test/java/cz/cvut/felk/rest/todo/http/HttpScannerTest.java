package cz.cvut.felk.rest.todo.http;

import static org.junit.Assert.*;

import org.junit.Test;

import cz.cvut.felk.rest.todo.http.HttpScanner.LexType;
import cz.cvut.felk.rest.todo.http.HttpScanner.LexUnit;

public class HttpScannerTest {

	@Test
	public void testReadNullInput() {
		HttpScanner scanner = new HttpScanner(null);
		assertNull(scanner.read());
	}
	
	@Test
	public void testReadCRLF() {
		HttpScanner scanner = new HttpScanner("\r\n");
		LexUnit crlf = scanner.read();
		
		assertNotNull(crlf);
		assertTrue(crlf.getTypes().contains(LexType.CR));
		assertTrue(crlf.getTypes().contains(LexType.LF));
		assertTrue(crlf.getTypes().contains(LexType.CRLF));
		
		assertEquals(2, crlf.getLength());
		assertArrayEquals("\r\n".getBytes(HttpScanner.US_ASCII_CHARSET), crlf.getValue());
		
		LexUnit eof = scanner.read();
		assertNull(eof);
	}
	
}
