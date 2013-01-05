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
package cz.cvut.felk.rest.todo.http.headers;

import cz.cvut.felk.rest.todo.http.lang.HttpLexScanner;
import cz.cvut.felk.rest.todo.http.lang.HttpLexUnit;


public class HttpAcceptQValue {

	private final String attribute;
	private final String value;
	
	public HttpAcceptQValue(final String attribute, final String value) {
		super();
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * = "q" "=" qvalue
     * qvalue         = ( "0" [ "." 0*3DIGIT ] )
     *                | ( "1" [ "." 0*3("0") ] )
	 */
	public static HttpAcceptQValue read(HttpLexScanner scanner) {
		if (scanner == null) {
			throw new IllegalArgumentException("The 'value' parameter cannot be a null.");
		}
		scanner.tx();

		String attribute = HttpLexUnit.readToken(scanner);
		if ("q".equalsIgnoreCase(attribute)) {
			if ((scanner.getAsChar(scanner.read()) == '=')) {
				HttpLexUnit d1 = scanner.read();
				if (d1 != null) {
					if ('0' == scanner.getAsChar(d1)) {
						
						
					} else if ('1' == scanner.getAsChar(d1)) {
						
					}
					
//					scanner.commit();
//					return new HttpAcceptQValue(attribute, value);
				}
			}

		}

		scanner.rollback();
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

	public Float getValue() {
		return 1f;	//TODO
	}
}
