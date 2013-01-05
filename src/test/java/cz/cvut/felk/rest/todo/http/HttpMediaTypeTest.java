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
package cz.cvut.felk.rest.todo.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import cz.cvut.felk.rest.todo.http.headers.HttpMediaType;
import cz.cvut.felk.rest.todo.http.lang.HttpLexScanner;

public class HttpMediaTypeTest {

	@Test
	public void testReadNull() {
		assertNull(HttpMediaType.read(new HttpLexScanner(null)));
	}
	
	@Test
	public void testRead() {
		HttpMediaType t = HttpMediaType.read(new HttpLexScanner("application/json")); 
		assertNotNull(t);
		assertEquals("application", t.getType());
		assertEquals("json", t.getSubtype());
		assertNull(t.getParameters());
		
		HttpMediaType t2 = HttpMediaType.read(new HttpLexScanner("x/vnd.a.b.c+xml")); 
		assertNotNull(t2);
		assertEquals("x", t2.getType());
		assertEquals("vnd.a.b.c+xml", t2.getSubtype());
		assertNull(t2.getParameters());
	}
	
	@Test
	public void testReadWithParams() {
		HttpMediaType t = HttpMediaType.read(new HttpLexScanner("text/html; a=b;1234=\"\\\"5\"")); 
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
