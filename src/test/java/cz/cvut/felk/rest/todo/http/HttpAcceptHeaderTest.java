package cz.cvut.felk.rest.todo.http;

import static org.junit.Assert.*;
import org.junit.Test;

import cz.cvut.felk.rest.todo.http.headers.HttpAcceptHeader;
import cz.cvut.felk.rest.todo.http.lang.HttpLexScanner;

public class HttpAcceptHeaderTest {

	@Test
	public void testRead1() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("audio/*; q=0.2, audio/basic"));
		assertNotNull(a);
		assertNotNull(a.getItems());
		assertEquals(2, a.getItems().length);
		
		assertNotNull(a.getItems()[0]);
		assertNotNull(a.getItems()[0].getRange());
		assertEquals("audio", a.getItems()[0].getRange().getType());
		assertEquals("*", a.getItems()[0].getRange().getSubtype());
		assertNull(a.getItems()[0].getRange().getParameters());
		
		assertEquals(0.2f, a.getItems()[0].getQualityFactor(), 0);
		
		assertNotNull(a.getItems()[1]);
		assertNotNull(a.getItems()[1].getRange());
		assertEquals("audio", a.getItems()[1].getRange().getType());
		assertEquals("basic", a.getItems()[1].getRange().getSubtype());
		assertNull(a.getItems()[1].getRange().getParameters());
		assertEquals(1f, a.getItems()[1].getQualityFactor(), 0);
	}

	@Test
	public void testRead2() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("text/plain; q=0.5, text/html, text/x-dvi; q=0.8, text/x-c"));
		assertNotNull(a);
	}
	
	@Test
	public void testRead3() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("text/*, text/html, text/html;level=1, */*"));
		assertNotNull(a);
	}
	
	@Test
	public void testRead4() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;level=2;q=0.4, */*;q=0.5"));
		assertNotNull(a);
	}

	@Test
	public void testRead5() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		assertNotNull(a);
	}
	
	@Test
	public void testRead6() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("application/xml,application/xhtml+xml,text/html;q=0.9,\rtext/plain;q=0.8,image/png,*/*;q=0.5"));
		assertNotNull(a);
	}
	
	@Test
	public void testRead7() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("image/jpeg, application/x-ms-application, image/gif,\rapplication/xaml+xml, image/pjpeg, application/x-ms-xbap,\rapplication/x-shockwave-flash, application/msword, */*"));
		assertNotNull(a);
	}

	@Test
	public void testRead8() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("application/vnd.ms-xpsdocument, application/xaml+xml, application/x-ms-xbap, application/x-shockwave-flash, application/x-silverlight-2-b2, application/x-silverlight, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"));
		assertNotNull(a);
	}
	
	@Test
	public void testRead9() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("text/plain"));
		assertNotNull(a);
	}

	@Test
	public void testRead10() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("application/json, application/xml"));
		assertNotNull(a);
	}


}
