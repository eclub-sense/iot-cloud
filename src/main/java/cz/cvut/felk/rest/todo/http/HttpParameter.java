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

import java.text.ParseException;

public class HttpParameter {

	private final String attribute;
	private final String value;
	
	public HttpParameter(final String attribute, final String value) {
		super();
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * <ul>
	 *   <li>parameter               = attribute "=" value</li>
     *   <li>attribute               = token</li>
     *   <li>value                   = token | quoted-string</li>
     * </ul>
	 */
	public static HttpParameter valueOf(final String value) throws ParseException, IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException("The 'value' parameter cannot be a null.");
		}
		if (!value.contains("=")) {
			throw new ParseException(value, 0);	
		}
		
		String attribute = value.substring(0, value.indexOf("="));
		if (!HttpLang.isToken(attribute)) {
			throw new ParseException(value, 0);
		}
		
		String val = value.substring(value.indexOf("=") + 1);
		if (!HttpLang.isToken(val) && !HttpLang.isQuotedString(val)) {
			throw new ParseException(value, attribute.length());
		}
		return new HttpParameter(attribute.trim(), val.trim());
	}
	
	public static HttpParameter readParamater(HttpScanner scanner) {
		if (scanner == null) {
			throw new IllegalArgumentException("The 'value' parameter cannot be a null.");
		}
		
		HttpLang.skipWs(scanner);
		String atrribute = HttpLang.readToken(scanner);
		HttpLang.skipWs(scanner);
		
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(attribute);
		builder.append('=');
		builder.append(value);
		return builder.toString();
	}
	
	public String getAttribute() {
		return attribute;
	}

	public String getValue() {
		return value;
	}
}
