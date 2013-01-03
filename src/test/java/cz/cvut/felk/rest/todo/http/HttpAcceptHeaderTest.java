package cz.cvut.felk.rest.todo.http;

import static org.junit.Assert.*;
import org.junit.Test;

public class HttpAcceptHeaderTest {

	@Test
	public void testRead1() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpScanner("audio/*; q=0.2, audio/basic"));
		assertNotNull(a);
	}

	@Test
	public void testRead2() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpScanner("text/plain; q=0.5, text/html, text/x-dvi; q=0.8, text/x-c"));
		assertNotNull(a);
	}
	
	@Test
	public void testRead3() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpScanner("text/*, text/html, text/html;level=1, */*"));
		assertNotNull(a);
	}
	
	@Test
	public void testRead4() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpScanner("text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;level=2;q=0.4, */*;q=0.5"));
		assertNotNull(a);
	}

	@Test
	public void testRead5() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpScanner("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		assertNotNull(a);
	}
	
	@Test
	public void testRead6() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpScanner("application/xml,application/xhtml+xml,text/html;q=0.9,\rtext/plain;q=0.8,image/png,*/*;q=0.5"));
		assertNotNull(a);
	}
	
	@Test
	public void testRead7() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpScanner("image/jpeg, application/x-ms-application, image/gif,\rapplication/xaml+xml, image/pjpeg, application/x-ms-xbap,\rapplication/x-shockwave-flash, application/msword, */*"));
		assertNotNull(a);
	}

	@Test
	public void testRead8() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpScanner("application/vnd.ms-xpsdocument, application/xaml+xml, application/x-ms-xbap, application/x-shockwave-flash, application/x-silverlight-2-b2, application/x-silverlight, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"));
		assertNotNull(a);
	}
}
