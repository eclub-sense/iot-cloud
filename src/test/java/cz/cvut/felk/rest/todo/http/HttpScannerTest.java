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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cz.cvut.felk.rest.todo.http.HttpLexUnit.Type;

public class HttpScannerTest {

	@Test
	public void testReadNullInput() {
		HttpLexScanner scanner = new HttpLexScanner(null);
		assertNull(scanner.read());
		assertTrue(scanner.isEof());
	}
	
	@Test
	public void testReadCRLF() {
		HttpLexScanner scanner = new HttpLexScanner("\r\n");
		HttpLexUnit crlf = scanner.read();
		
		assertNotNull(crlf);
		assertTrue(crlf.getTypes().contains(Type.CR));
		assertTrue(crlf.getTypes().contains(Type.LF));
		assertTrue(crlf.getTypes().contains(Type.CRLF));
		
		assertEquals(2, crlf.getLength());
		assertEquals(0, crlf.getIndex());
	
		String value = scanner.getAsString(crlf.getIndex(), crlf.getLength());
		assertEquals("\r\n", value);
		
		HttpLexUnit eof = scanner.read();
		assertNull(eof);
		assertTrue(scanner.isEof());
	}
	
	@Test
	public void testRollback() {
		HttpLexScanner scanner = new HttpLexScanner("a\\c");
		scanner.tx();
		
		assertNotNull(scanner.read());	// read a
		assertNotNull(scanner.read());	// read \\
		
		scanner.rollback();
		
		assertNotNull(scanner.read());	// read a
		assertNotNull(scanner.read());	// read \\
		assertNotNull(scanner.read());	// read b
		assertNull(scanner.read());
		assertTrue(scanner.isEof());
	}
	
	@Test
	public void testReadParameter() {
		HttpLexScanner scanner = new HttpLexScanner("a=b");
		HttpLexUnit a = scanner.read();
		assertNotNull(a);
		
		HttpLexUnit e = scanner.read();
		assertNotNull(e);
		
		HttpLexUnit b = scanner.read();
		assertNotNull(b);
		
		HttpLexUnit eof = scanner.read();
		assertNull(eof);
		assertTrue(scanner.isEof());
	}
}
