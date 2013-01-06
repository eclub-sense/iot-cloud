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

import java.util.ArrayList;
import java.util.List;

import org.sprintapi.api.http.lang.HttpLexScanner;
import org.sprintapi.api.http.lang.HttpLexUnit;


public class HttpMediaType {

	protected final String type;
	protected final String subtype;

	private final HttpParameter[] parameters;

	public HttpMediaType(String type, String subtype) {
		this(type, subtype, null);
	}
	public HttpMediaType(String type, String subtype, HttpParameter[] parameters) {
		super();
		this.type = type;
		this.subtype = subtype;
		this.parameters = parameters;
	}

	public String getType() {
		return type;
	}

	public String getSubtype() {
		return subtype;
	}
	
	public HttpParameter[] getParameters() {
		return parameters;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj != null) && (obj instanceof HttpMediaType) && (toString().equals(obj.toString()));
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(type);
		builder.append('/');
		builder.append(subtype);
		
		if (parameters != null) {
			for (HttpParameter parameter : parameters) {
				builder.append(';');
				builder.append(parameter.toString());
			}
		}
		return builder.toString();
	}
	
	/**
	 * <ul>
	 *   <li>media-type     = type "/" subtype *( ";" parameter )</li>
     *   <li>type           = token</li>
     *   <li>subtype        = token</li>
     * </ul>
	 */
	public static HttpMediaType read(HttpLexScanner scanner) throws IllegalArgumentException {
		if (scanner == null) {
			throw new IllegalArgumentException("The 'scanner' parameter cannot be a null.");
		}

		scanner.tx();
		
		String type = HttpLexUnit.readToken(scanner);
		if ((type == null) || ('/' != scanner.getAsChar(scanner.read()))) {
			scanner.rollback();
			return null;
		}
		
		String subtype = HttpLexUnit.readToken(scanner);
		if  (subtype == null) {
			scanner.rollback();
			return null;			
		}
		
		scanner.commit();
		
		List<HttpParameter> params = new ArrayList<HttpParameter>();
		
		scanner.tx();
		while (';' == scanner.getAsChar(scanner.read())) {
			scanner.tx();
			HttpLexUnit sp = scanner.read();
			while (sp != null && sp.isType(HttpLexUnit.Type.SP)) {
				scanner.commit();
				scanner.tx();
				sp = scanner.read();
			}
			scanner.rollback();
			
			HttpParameter param = HttpParameter.read(scanner);
			if (param != null) {
				params.add(param);
				scanner.commit();
				scanner.tx();
			}
		}
		scanner.rollback();
		
		return new HttpMediaType(type, subtype,
				params.isEmpty() ? null : params.toArray(new HttpParameter[params.size()]));
	}
	
	public boolean isValid() {
		return (type != null) && (subtype != null);
	}
}
