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
package cz.cvut.felk.rest.todo.api.http.headers;

import cz.cvut.felk.rest.todo.api.http.lang.HttpLexScanner;
import cz.cvut.felk.rest.todo.api.http.lang.HttpLexUnit;


public class HttpAcceptExtension {

	private final String attribute;
	private final String value;
	
	public HttpAcceptExtension(final String attribute, final String value) {
		super();
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * accept-extension =  token [ "=" ( token | quoted-string ) ]
	 */
	public static HttpAcceptExtension read(HttpLexScanner scanner) {
		if (scanner == null) {
			throw new IllegalArgumentException("The 'value' parameter cannot be a null.");
		}
		scanner.tx();

		String attribute = HttpLexUnit.readToken(scanner);
		if (attribute == null) {
			scanner.rollback();
			return null;
		}
		scanner.commit();
		
		scanner.tx();
		if ((scanner.getAsChar(scanner.read()) != '=')) {
			scanner.rollback();
			return new HttpAcceptExtension(attribute, null);
		}

		String value = HttpLexUnit.readToken(scanner);
		if (value == null) {
			value = HttpLexUnit.readQuotedString(scanner);
			if (value == null) {
				scanner.rollback();
				return null;
			}
		}
		
		scanner.commit();
		return new HttpAcceptExtension(attribute, value);
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
