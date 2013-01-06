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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.cvut.felk.rest.todo.api.http.lang.HttpLexScanner;
import cz.cvut.felk.rest.todo.api.http.lang.HttpLexUnit;

public class HttpAcceptHeader {

	private final HttpAcceptHeaderItem[] items;
	
	public HttpAcceptHeader(HttpAcceptHeaderItem[] items) {
		super();
		this.items = items;
	}
	
	public HttpAcceptHeaderItem[] getItems() {
		return items;
	}

	public static final HttpAcceptHeader read(String value) throws IllegalArgumentException {
		return value != null ? read(new HttpLexScanner(value.trim())) : null;
	}
	
	/**
	 * Accept         = "#( media-range [ accept-params ] )
     *    accept-params  = ";" "q" "=" qvalue *( accept-extension )
     *    accept-extension = ";" token [ "=" ( token | quoted-string ) ] 
	 */
	public static final HttpAcceptHeader read(HttpLexScanner scanner) throws IllegalArgumentException {
		if (scanner == null) {
			throw new IllegalArgumentException("The 'scanner' parameter cannot be a null.");
		}

		List<HttpAcceptHeaderItem> items = new ArrayList<HttpAcceptHeaderItem>();
		HttpAcceptHeaderItem item = null;
		HttpLexUnit unit = null;
		
		scanner.tx();
		
		do {
			HttpLexUnit.skipWs(scanner);
			
			item = HttpAcceptHeaderItem.read(scanner);
			if (item != null) {
				items.add(item);
			}
			HttpLexUnit.skipWs(scanner);
			
			unit = scanner.read();
			
	    } while ((item != null) && (unit != null) && (',' == scanner.getAsChar(unit)));
	
		
		if (items.isEmpty()) {
			scanner.rollback();
			return null;
		}
		scanner.commit();
		
		Collections.sort(items, new Comparator<HttpAcceptHeaderItem>() {
			@Override
			public int compare(HttpAcceptHeaderItem o1, HttpAcceptHeaderItem o2) {
				return (int) ((o2.getQualityFactor() - o1.getQualityFactor()) * 1000f);
			}
		});
		
		return new HttpAcceptHeader(items.toArray(new HttpAcceptHeaderItem[0]));
	}

	public int accept(String mediaType) {
		int weight = 0;
		if (items != null) {
			int i = items.length;
			for (HttpAcceptHeaderItem item : items) {
				if ((i > weight) && item.getRange().match(mediaType)) {
					weight = i;
				}
				i -= 1;
			}
		}
		return weight;
	}
}
