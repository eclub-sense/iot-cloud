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
package org.sprintapi.api.http.headers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sprintapi.api.http.headers.HttpMediaRange;
import org.sprintapi.api.http.lang.HttpLexScanner;


public class HttpMediaRangeTest {

	@Test
	public void testReadNull() {
		assertNull(HttpMediaRange.read(new HttpLexScanner(null)));
	}
		
	@Test
	public void testReadAnyAny() {
	 	HttpMediaRange range = HttpMediaRange.read(new HttpLexScanner("*/*"));
	 	assertNotNull(range);
		assertEquals("*", range.getType());
		assertEquals("*", range.getSubtype());
		assertNull(range.getParameters());

	}
	
	@Test
	public void testReadAny() {
		HttpMediaRange range = HttpMediaRange.read(new HttpLexScanner("text/*"));
		assertNotNull(range);
		assertEquals("text", range.getType());
		assertEquals("*", range.getSubtype());
		assertNull(range.getParameters());
	}	
	
	@Test
	public void testMatch() {
		HttpMediaRange range = HttpMediaRange.read(new HttpLexScanner("application/*"));
		assertNotNull(range);
		assertTrue(range.match("application/vnd.x+json; level=3"));
		assertTrue(range.match("application/json; charset=utf-8"));
		assertFalse(range.match("text/plain"));
	}

}
