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
package org.sprintapi.api.http.headers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.sprintapi.api.http.headers.HttpParameter;
import org.sprintapi.api.http.lang.HttpLexScanner;


public class HttpParameterTest {

	@Test
	public void testReadNull() {
		assertNull(HttpParameter.read(new HttpLexScanner(null)));
	}
	
	@Test
	public void testReadToken() {
		HttpParameter param = HttpParameter.read(new HttpLexScanner("a=b"));
		assertNotNull(param);
		assertEquals("a", param.getAttribute());
		assertEquals("b", param.getValue());

		HttpParameter param2 = HttpParameter.read(new HttpLexScanner("1adf=234"));
		assertNotNull(param2);
		assertEquals("1adf", param2.getAttribute());
		assertEquals("234", param2.getValue());
	}

	@Test
	public void testReadString() {
		HttpParameter param = HttpParameter.read(new HttpLexScanner("a=\"b\""));
		assertNotNull(param);
		assertEquals("a", param.getAttribute());
		assertEquals("\"b\"", param.getValue());

		HttpParameter param2 = HttpParameter.read(new HttpLexScanner("1adf=\"234\""));
		assertNotNull(param2);
		assertEquals("1adf", param2.getAttribute());
		assertEquals("\"234\"", param2.getValue());
	}

	@Test
	public void testReadEscString() {
		HttpParameter param = HttpParameter.read(new HttpLexScanner("a=\"\\\"b\""));
		assertNotNull(param);
		assertEquals("a", param.getAttribute());
		assertEquals("\"\\\"b\"", param.getValue());

		HttpParameter param2 = HttpParameter.read(new HttpLexScanner("1adf=\"23\'4\""));
		assertNotNull(param2);
		assertEquals("1adf", param2.getAttribute());
		assertEquals("\"23\'4\"", param2.getValue());
	}
}
