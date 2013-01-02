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
import java.util.ArrayList;
import java.util.List;

public class HttpMediaType {

	protected final String type;
	protected final String subtype;

	private final HttpParameter[] parameters;
	
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
     *
	 * @param mediaType
	 * @return
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 */
	public static HttpMediaType valueOf(String mediaType) throws ParseException, IllegalArgumentException {
		if (mediaType == null) {
			throw new IllegalArgumentException("The 'mediaType' parameter cannot be a null.");
		}
		if (!mediaType.contains("/")) {
			throw new ParseException(mediaType, 0);
		}
		
		String type = mediaType.substring(0, mediaType.indexOf('/')).trim();
				
		if (!HttpLang.isToken(type)) {
			throw new ParseException(mediaType, 0);
		}
		
		String subtype = mediaType.substring(mediaType.indexOf('/') + 1).trim();
		
		
		HttpParameter[] parameters = null;

		while ((subtype != null) && subtype.contains(";")) {
			List<HttpParameter> paramList = new ArrayList<HttpParameter>();
			
			subtype = subtype.substring(mediaType.indexOf(';') + 1).trim();
			
			for (String p : pair[1].substring(pair[1].indexOf(';') + 1).split(";")) {
				if ((p != null) && !p.trim().isEmpty()) {
					paramList.add(p.trim());
				}
			}
			if (!paramList.isEmpty()) {
				parameters = paramList.toArray(new String[paramList.size()]);
			}
			pair[1] = pair[1].substring(0, pair[1].indexOf(';'));		
		}
		
		if (!HttpLang.isToken(subtype)) {
			throw new ParseException(mediaType, mediaType.indexOf('/'));
		}
		
		return new HttpMediaType(pair[0].trim(), pair[1].trim(), parameters);
	}
	
	public boolean isValid() {
		return (type != null) && (subtype != null);
	}



}
