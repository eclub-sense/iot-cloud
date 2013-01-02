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

public class HttpMediaRange extends HttpMediaType {

	public static final String ANY = "*";
	
	public static final HttpMediaRange ALL_MEDIA_TYPES = new HttpMediaRange(ANY, ANY, null); 
		
	public HttpMediaRange(String type, String subtype, HttpParameter[] parameters) {
		super(type, subtype, parameters);
	}

	public static HttpMediaRange valueOf(String mediaRange) throws ParseException, IllegalArgumentException {
		return null;
	}

	public boolean match(final HttpMediaType mediaType) {		
		return isValid() 
				&& (ANY.equals(type) 
						|| (type.equals(mediaType.getType()) && (ANY.equals(subtype) || subtype.equals(mediaType.getSubtype()))));
	}
	
	public boolean isValid() {
		return super.isValid() && ((ANY.equals(type) && ANY.equals(subtype)) || !ANY.equals(type));
	}
	
}
