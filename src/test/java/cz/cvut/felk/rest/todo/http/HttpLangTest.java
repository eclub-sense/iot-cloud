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

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

import cz.cvut.felk.rest.todo.http.HttpScanner.LexType;
import cz.cvut.felk.rest.todo.http.HttpScanner.LexUnit;

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
	
	@Test
	public void testReadParameter()  throws IllegalArgumentException, ParseException {
		HttpScanner scanner = new HttpScanner("a=b");
		
		String a = HttpLang.readToken(scanner);
		assertEquals("a", a);
		
		LexUnit e = scanner.read();
		assertNotNull(e);
		assertEquals(1, e.getIndex());
		assertEquals(1, e.getLength());
		assertTrue(e.isType(LexType.SEPARATOR));
		
		String b = HttpLang.readToken(scanner);
		assertEquals("b", b);
		
		LexUnit eof = scanner.read();
		assertNull(eof);
	}
}
