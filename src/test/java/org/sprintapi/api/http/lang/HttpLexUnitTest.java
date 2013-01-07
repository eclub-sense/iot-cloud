/*
 *  Copyright 2012 the original author or authors.
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
package org.sprintapi.api.http.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;
import org.sprintapi.api.http.lang.HttpLexScanner;
import org.sprintapi.api.http.lang.HttpLexUnit;
import org.sprintapi.api.http.lang.HttpLexUnit.Type;


public class HttpLexUnitTest {

	@Test
	public void testReadNullToken() throws IllegalArgumentException, ParseException {
		 assertNull(HttpLexUnit.readToken(new HttpLexScanner(null)));
	}
	 
	@Test
	public void testReadToken() throws IllegalArgumentException, ParseException {
		 String token = HttpLexUnit.readToken(new HttpLexScanner("abcd"));
		 
		 assertNotNull(token);
		 assertEquals("abcd", token);
	}

	@Test
	public void testInvalidReadToken() throws IllegalArgumentException, ParseException {
		 assertNull(HttpLexUnit.readToken(new HttpLexScanner("/abcd")));
		 assertNull(HttpLexUnit.readToken(new HttpLexScanner(" abcd")));
		 assertNull(HttpLexUnit.readToken(new HttpLexScanner("")));
		 assertNull(HttpLexUnit.readToken(new HttpLexScanner("\t")));
	}

	@Test
	public void testReadTokens()  throws IllegalArgumentException, ParseException {
		HttpLexScanner scanner = new HttpLexScanner("a=b");
		
		String a = HttpLexUnit.readToken(scanner);
		assertEquals("a", a);
		
		HttpLexUnit e = scanner.read();
		assertNotNull(e);
		assertEquals(1, e.getIndex());
		assertEquals(1, e.getLength());
		assertTrue(e.isType(Type.SEPARATOR));
		
		String b = HttpLexUnit.readToken(scanner);
		assertEquals("b", b);
		
		HttpLexUnit eof = scanner.read();
		assertNull(eof);
	}
}
