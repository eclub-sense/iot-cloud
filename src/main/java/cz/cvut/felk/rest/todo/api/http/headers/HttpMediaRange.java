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

import java.util.ArrayList;
import java.util.List;

import cz.cvut.felk.rest.todo.api.http.lang.HttpLexScanner;
import cz.cvut.felk.rest.todo.api.http.lang.HttpLexUnit;


public class HttpMediaRange extends HttpMediaType {

	public static final String ANY = "*";
	
	public static final HttpMediaRange ALL_MEDIA_TYPES = new HttpMediaRange(ANY, ANY, null); 
	
	public HttpMediaRange(String type, String subtype, HttpParameter[] parameters) {
		super(type, subtype, parameters);
	}
	
	/**
	 * media-range    = ( "*&#47;*"
     *                  | ( type "/" "*" )
     *                  | ( type "/" subtype )
     *                  ) *( ";" parameter )
	 */
	public static HttpMediaRange read(HttpLexScanner scanner) throws IllegalArgumentException {
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
			if ((param != null) && (!"q".equalsIgnoreCase(param.getAttribute()))) {
				params.add(param);
				scanner.commit();
				scanner.tx();
			}
		}
		scanner.rollback();
		
		return new HttpMediaRange(type, subtype,
				params.isEmpty() ? null : params.toArray(new HttpParameter[params.size()]));
	}
	
	public boolean match(final HttpMediaType mediaType) {		
		return isValid() 
				&& (ANY.equals(type) 
						|| (type.equals(mediaType.getType()) && (ANY.equals(subtype) || subtype.equals(mediaType.getSubtype()))));
	}
	

	public boolean isValid() {
		return super.isValid() && ((ANY.equals(type) && ANY.equals(subtype)) || !ANY.equals(type));
	}

	public boolean match(String mediaType) {
		HttpMediaType mt = HttpMediaType.read(new HttpLexScanner(mediaType));
		if (mt != null) {
			return match(mt);
		}
		
		return false;
	}

}
