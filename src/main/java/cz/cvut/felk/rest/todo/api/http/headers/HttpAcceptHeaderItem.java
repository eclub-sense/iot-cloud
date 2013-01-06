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



public class HttpAcceptHeaderItem {

	private final HttpMediaRange range;
	private final Float qualityFactor;
	private final HttpAcceptExtension[] extensions;
	
	public HttpAcceptHeaderItem(HttpMediaRange range, Float qualityFactor, HttpAcceptExtension[] extensions) {
		super();
		this.range = range;
		this.qualityFactor = qualityFactor;
		this.extensions = extensions;
	}

	public HttpMediaRange getRange() {
		return range;
	}

	public Float getQualityFactor() {
		return qualityFactor;
	}

	public HttpAcceptExtension[] getExtensions() {
		return extensions;
	}
	
	/**
	 * Accept         =  media-range [ accept-params ] 
     *    accept-params  = ";" "q" "=" qvalue *( accept-extension )
     *    accept-extension = ";" token [ "=" ( token | quoted-string ) ] 
	 */
	public static final HttpAcceptHeaderItem read(HttpLexScanner scanner) throws IllegalArgumentException {
		if (scanner == null) {
			throw new IllegalArgumentException("The 'scanner' parameter cannot be a null.");
		}

		scanner.tx();
		
		HttpMediaRange mediaRange = HttpMediaRange.read(scanner);

		if (mediaRange != null) {
			scanner.commit();
			scanner.tx();
			
			HttpLexUnit.skipWs(scanner);
			
			HttpLexUnit qdel = scanner.read();

			if ((qdel != null) && (';' == scanner.getAsChar(qdel))) {
				
				HttpLexUnit.skipWs(scanner);
				
				HttpAcceptQValue qParam = HttpAcceptQValue.read(scanner);

				if (qParam != null) {
					scanner.commit();
					return new HttpAcceptHeaderItem(mediaRange, qParam.getValue(), null);
				}
			}
			scanner.rollback();
			return new HttpAcceptHeaderItem(mediaRange, 1f, null);			
		}
		scanner.rollback();
		return null;
	}
	
	@Override
	public String toString() {
		return ((range != null) ? range.toString() + ";q=" + qualityFactor : super.toString());
	}
}