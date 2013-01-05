/*
 *  Copyright 2012 sprintapi.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cz.cvut.felk.rest.todo.http.headers;

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
		assertNotNull(a.getItems());
		assertEquals(4, a.getItems().length);
		
		assertNotNull(a.getItems()[0]);
		assertNotNull(a.getItems()[0].getRange());
		assertEquals("text", a.getItems()[0].getRange().getType());
		assertEquals("plain", a.getItems()[0].getRange().getSubtype());
		assertNull(a.getItems()[0].getRange().getParameters());		
		assertEquals(0.5f, a.getItems()[0].getQualityFactor(), 0);
		
		assertNotNull(a.getItems()[1]);
		assertNotNull(a.getItems()[1].getRange());
		assertEquals("text", a.getItems()[1].getRange().getType());
		assertEquals("html", a.getItems()[1].getRange().getSubtype());
		assertNull(a.getItems()[1].getRange().getParameters());		
		assertEquals(1f, a.getItems()[1].getQualityFactor(), 0);
		
		assertNotNull(a.getItems()[2]);
		assertNotNull(a.getItems()[2].getRange());
		assertEquals("text", a.getItems()[2].getRange().getType());
		assertEquals("x-dvi", a.getItems()[2].getRange().getSubtype());
		assertNull(a.getItems()[2].getRange().getParameters());		
		assertEquals(0.8f, a.getItems()[2].getQualityFactor(), 0);
		
		assertNotNull(a.getItems()[3]);
		assertNotNull(a.getItems()[3].getRange());
		assertEquals("text", a.getItems()[3].getRange().getType());
		assertEquals("x-c", a.getItems()[3].getRange().getSubtype());
		assertNull(a.getItems()[3].getRange().getParameters());		
		assertEquals(1f, a.getItems()[3].getQualityFactor(), 0);
	}
	
	@Test
	public void testRead3() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("text/*, text/html, text/html;level=1, */*"));
		assertNotNull(a);
		assertNotNull(a.getItems());
		assertEquals(4, a.getItems().length);
	}
	
	@Test
	public void testRead4() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;level=2;q=0.4, */*;q=0.5"));
		assertNotNull(a);
		assertNotNull(a.getItems());
		assertEquals(5, a.getItems().length);

	}

	@Test
	public void testRead5() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		assertNotNull(a);
		assertNotNull(a.getItems());
		assertEquals(4, a.getItems().length);

	}
	
	@Test
	public void testRead6() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("application/xml,application/xhtml+xml,text/html;q=0.9, text/plain;q=0.8,image/png,*/*;q=0.5"));
		assertNotNull(a);
		assertNotNull(a.getItems());
		assertEquals(6, a.getItems().length);

	}
	
	@Test
	public void testRead7() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("image/jpeg, application/x-ms-application, image/gif,application/xaml+xml, image/pjpeg, application/x-ms-xbap,application/x-shockwave-flash, application/msword, */*"));
		assertNotNull(a);
		assertNotNull(a.getItems());
		assertEquals(9, a.getItems().length);
	}

	@Test
	public void testRead8() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("application/vnd.ms-xpsdocument, application/xaml+xml, application/x-ms-xbap, application/x-shockwave-flash, application/x-silverlight-2-b2, application/x-silverlight, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"));
		assertNotNull(a);
		
		assertNotNull(a.getItems());
		assertEquals(10, a.getItems().length);
	}

	@Test
	public void testRead9() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("text/plain"));
		assertNotNull(a);
		assertNotNull(a.getItems());
		assertEquals(1, a.getItems().length);
	}

	@Test
	public void testRead10() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("application/json, application/xml"));
		assertNotNull(a);
		assertNotNull(a.getItems());
		assertEquals(2, a.getItems().length);
	}

	@Test 
	public void testAccept() {
		HttpAcceptHeader a = HttpAcceptHeader.read(new HttpLexScanner("application/vnd.x+json, application/xml; q=0.5, application/json"));
		assertNotNull(a);
		
		assertEquals(3, a.accept("application/vnd.x+json"));
		assertEquals(2, a.accept("application/json"));
		assertEquals(1, a.accept("application/xml"));
		assertEquals(0, a.accept("text/html"));
	}

}
