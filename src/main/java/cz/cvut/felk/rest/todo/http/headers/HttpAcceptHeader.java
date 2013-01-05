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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ext.LexicalHandler;

import cz.cvut.felk.rest.todo.http.lang.HttpLexScanner;
import cz.cvut.felk.rest.todo.http.lang.HttpLexUnit;


public class HttpAcceptHeader {

	private final HttpAcceptHeaderItem[] HttpAcceptHeaderItems;
	
	public HttpAcceptHeader(HttpAcceptHeaderItem[] HttpAcceptHeaderItems) {
		super();
		this.HttpAcceptHeaderItems = HttpAcceptHeaderItems;
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
		scanner.tx();
		do {
			HttpMediaRange mediaRange = HttpMediaRange.read(scanner);
			if (mediaRange != null) {
				HttpLexUnit qdel = scanner.read();
				if ((qdel != null) && (';' == scanner.getAsChar(qdel))) {
					
					HttpAcceptQValue qParam = HttpAcceptQValue.read(scanner);
					if (qParam != null) {
						items.add(new HttpAcceptHeaderItem(mediaRange, qParam.getValue(), null));
					}
				}
			} else {
				scanner.rollback();
				return null;
			}
	    } while (scanner.isEof());
		
		if (items.isEmpty()) {
			scanner.rollback();
			return null;
		}

		scanner.commit();
		return new HttpAcceptHeader(items.toArray(new HttpAcceptHeaderItem[0]));
	}
}
