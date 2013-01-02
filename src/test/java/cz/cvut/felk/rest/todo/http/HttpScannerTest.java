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
		assertEquals(0, crlf.getIndex());
	
		String value = scanner.getAsString(crlf.getIndex(), crlf.getLength());
		assertEquals("\r\n", value);
		
		LexUnit eof = scanner.read();
		assertNull(eof);
	}
	
	@Test
	public void testRollback() {
		HttpScanner scanner = new HttpScanner("a\\c");

		assertNotNull(scanner.read());	// read a
		assertNotNull(scanner.read());	// read \\
		
		scanner.rollback(2);
		assertNotNull(scanner.read());	// read a
		assertNotNull(scanner.read());	// read \\
		assertNotNull(scanner.read());	// read b
		assertNull(scanner.read());
	}
}
