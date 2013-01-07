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

import java.text.ParseException;

import org.junit.Test;
import org.sprintapi.api.http.headers.HttpAcceptQValue;
import org.sprintapi.api.http.lang.HttpLexScanner;


public class HttpAcceptQValueTest {

	@Test
	public void testReadNullToken() throws IllegalArgumentException, ParseException {
		 assertNull(HttpAcceptQValue.read(new HttpLexScanner(null)));
	}
	
	@Test
	public void testRead1() throws IllegalArgumentException, ParseException {
		 HttpAcceptQValue qvalue = HttpAcceptQValue.read(new HttpLexScanner("q=1"));
		 assertNotNull(qvalue);
		 assertEquals(1f, qvalue.getValue(), 0);
	}
	
	@Test
	public void testRead2() throws IllegalArgumentException, ParseException {
		 HttpAcceptQValue qvalue = HttpAcceptQValue.read(new HttpLexScanner("q=1."));
		 assertNotNull(qvalue);
		 assertEquals(1f, qvalue.getValue(), 0);
	}

	@Test
	public void testRead3() throws IllegalArgumentException, ParseException {
		 HttpAcceptQValue qvalue = HttpAcceptQValue.read(new HttpLexScanner("q=1.000"));
		 assertNotNull(qvalue);
		 assertEquals(1f, qvalue.getValue(), 0);
	}
	
	@Test
	public void testRead4() throws IllegalArgumentException, ParseException {
		 HttpAcceptQValue qvalue = HttpAcceptQValue.read(new HttpLexScanner("q=0.2"));
		 assertNotNull(qvalue);
		 assertEquals(0.2f, qvalue.getValue(), 0);
	}
	
	@Test
	public void testRead5() throws IllegalArgumentException, ParseException {
		 HttpAcceptQValue qvalue = HttpAcceptQValue.read(new HttpLexScanner("q=0.090"));
		 assertNotNull(qvalue);
		 assertEquals(0.09f, qvalue.getValue(), 0);
	}
}
