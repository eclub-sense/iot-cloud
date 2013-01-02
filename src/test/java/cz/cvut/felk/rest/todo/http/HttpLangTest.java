package cz.cvut.felk.rest.todo.http;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

public class HttpLangTest {

	@Test
	public void testReadNullToken() throws IllegalArgumentException, ParseException {
		 assertNull(HttpLang.readToken(new HttpScanner(null)));
	}
	
	@Test
	public void testReadToken() throws IllegalArgumentException, ParseException {
		 String token = HttpLang.readToken(new HttpScanner("abcd"));
		 
		 assertNotNull(token);
		 assertEquals("abcd", token);
	}

	@Test
	public void testInvalidReadToken() throws IllegalArgumentException, ParseException {
		 assertNull(HttpLang.readToken(new HttpScanner("/abcd")));
		 assertNull(HttpLang.readToken(new HttpScanner(" abcd")));
		 assertNull(HttpLang.readToken(new HttpScanner("")));
		 assertNull(HttpLang.readToken(new HttpScanner("\t")));
	}
}
