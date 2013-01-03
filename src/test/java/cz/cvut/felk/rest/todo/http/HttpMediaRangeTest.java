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

public class HttpMediaRangeTest {

	@Test
	public void testReadNull() {
		assertNull(HttpMediaRange.read(new HttpScanner(null)));
	}
		
	@Test
	public void testReadAnyAny() {
	 	HttpMediaRange range = HttpMediaRange.read(new HttpScanner("*/*"));
	 	assertNotNull(range);
		assertEquals("*", range.getType());
		assertEquals("*", range.getSubtype());
		assertNull(range.getParameters());

	}
	
	@Test
	public void testReadAny() {
		HttpMediaRange range = HttpMediaRange.read(new HttpScanner("text/*"));
		assertNotNull(range);
		assertEquals("text", range.getType());
		assertEquals("*", range.getSubtype());
		assertNull(range.getParameters());

	}	

}
