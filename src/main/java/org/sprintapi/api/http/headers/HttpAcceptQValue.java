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

import org.sprintapi.api.http.lang.HttpLexScanner;
import org.sprintapi.api.http.lang.HttpLexUnit;


public class HttpAcceptQValue {

	private final Float value;
	
	public HttpAcceptQValue(final Float value) {
		super();
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

		HttpLexUnit attribute = scanner.read();
		if ('q' == scanner.getAsChar(attribute)) {
			if ((scanner.getAsChar(scanner.read()) == '=')) {
				HttpLexUnit d1 = scanner.read();
				if (d1 != null) {
					if ('0' == scanner.getAsChar(d1)) {
						scanner.commit();
						scanner.tx();

						HttpLexUnit dl = scanner.read();
						if ((dl == null) || ('.' != scanner.getAsChar(dl))) {
							scanner.rollback();
							return new HttpAcceptQValue(0f);
						}
						
						scanner.commit();
						scanner.tx();
						
						String dv = "";
						
						for (int i=0; i<3; i++) {
							HttpLexUnit dx = scanner.read();
							if ((dx == null) || !dx.isType(HttpLexUnit.Type.DIGIT)) {
								scanner.rollback();
								return new HttpAcceptQValue(dv.isEmpty() ? 0f : Float.parseFloat("0." + dv));
							}
							dv += scanner.getAsChar(dx);
							scanner.tx();
						}
						
						scanner.commit();
						return new HttpAcceptQValue(dv.isEmpty() ? 0f : Float.parseFloat("0." + dv));
						
					} else if ('1' == scanner.getAsChar(d1)) {
						
						scanner.commit();
						scanner.tx();

						HttpLexUnit dl = scanner.read();
						if ((dl == null) || ('.' != scanner.getAsChar(dl))) {
							scanner.rollback();
							return new HttpAcceptQValue(1f);
						}
						
						scanner.commit();
						scanner.tx();

						for (int i=0; i<3; i++) {
							HttpLexUnit dx = scanner.read();
							if ((dx == null) || ('0' != scanner.getAsChar(dx))) {
								scanner.rollback();
								return new HttpAcceptQValue(1f);
							}
							scanner.tx();
						}
						
						scanner.commit();
						return new HttpAcceptQValue(1f); 
					}
				}
			}

		}

		scanner.rollback();
		return null;		
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('q');
		builder.append('=');
		builder.append(value);
		return builder.toString();
	}

	public Float getValue() {
		return value;
	}
}
