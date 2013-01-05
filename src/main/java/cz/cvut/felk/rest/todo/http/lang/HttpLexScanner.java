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
package cz.cvut.felk.rest.todo.http.lang;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import cz.cvut.felk.rest.todo.http.lang.HttpLexUnit.Type;

public class HttpLexScanner {
	
	private final byte[] bytes;
	private int byteCursor;
	
	private final HttpLexUnit[] units;
	private int unitCursor;
	
	private Stack<Integer> tx = new Stack<Integer>();
	
	
	public HttpLexScanner(final String value) {
		super();
		this.bytes = (value != null) ? value.getBytes(HttpLexUnit.US_ASCII_CHARSET) : null;
		this.units = (bytes != null) ? new HttpLexUnit[bytes.length] : null;
		this.byteCursor = 0;
		this.unitCursor = 0;
	}
	
	public HttpLexUnit read() {
		if ((units == null) || (unitCursor >= units.length)) {
			return null;
		}
		
		if (units[unitCursor] == null) {
			units[unitCursor] = scan();
			if (units[unitCursor] == null) {
				return null;
			}
		}
		
		unitCursor += 1;
		return units[unitCursor -1];
	}

	public void tx() {
		tx.push(unitCursor);
	}
	
	public void commit() {
		tx.pop();
	}
	
	public void rollback() {
		unitCursor = tx.pop();
	}
	
	public boolean isEof() {
		return ((units == null) || (unitCursor >= units.length) || ((units[unitCursor] == null) && (byteCursor >= bytes.length)));
	}

	protected HttpLexUnit scan() {
		if ((bytes == null) || (byteCursor >= bytes.length)) {
			return null;
		}

		int index = byteCursor;
		Set<Type> types = new HashSet<HttpLexUnit.Type>();
		
		if (HttpLexUnit.isChar(bytes[byteCursor])) {
			types.add(Type.CHAR);
		}
		if (HttpLexUnit.isCr(bytes[byteCursor])) {
			types.add(Type.CR);
			if (((byteCursor + 1) < bytes.length) && HttpLexUnit.isLf(bytes[byteCursor+1])) {
				types.add(Type.LF);
				types.add(Type.CRLF);
				index += 1;

				if ((byteCursor + 2) < bytes.length) {
					if (HttpLexUnit.isHt(bytes[byteCursor+2])) {
						types.add(Type.HT);
						types.add(Type.LWS);
						index += 1;
						
					} else if (HttpLexUnit.isSp(bytes[byteCursor+2])) {
						types.add(Type.SP);
						types.add(Type.LWS);
						index += 1;
					}	
				}
			}
		}
		if (HttpLexUnit.isCtl(bytes[byteCursor])) {
			types.add(Type.CTL);
		}
		if (HttpLexUnit.isHt(bytes[byteCursor])) {
			types.add(Type.HT);
			types.add(Type.LWS);
		}
		if (HttpLexUnit.isLf(bytes[byteCursor])) {
			types.add(Type.LF);
		}
		if (HttpLexUnit.isSeparator(bytes[byteCursor])) {
			types.add(Type.SEPARATOR);
		}
		if (HttpLexUnit.isSp(bytes[byteCursor])) {
			types.add(Type.SP);
			types.add(Type.LWS);
		}
		if (HttpLexUnit.isAlpha(bytes[byteCursor])) {
			types.add(Type.ALPHA);
		}
		if (HttpLexUnit.isUpAlpha(bytes[byteCursor])) {
			types.add(Type.UPALPHA);
		}		
		if (HttpLexUnit.isLoAlpha(bytes[byteCursor])) {
			types.add(Type.LOALPHA);
		}
		if (HttpLexUnit.isDigit(bytes[byteCursor])) {
			types.add(Type.DIGIT);
		}
		if (HttpLexUnit.isHex(bytes[byteCursor])) {
			types.add(Type.HEX);
		}
		if (bytes[byteCursor] == 34) {
			types.add(Type.DQM);
		}
		
		index += 1;

		/*
		 * OCTET = <any 8-bit sequence of data>
		 */
		if ((index - byteCursor) == 1) {
			types.add(Type.OCTET);
		}

		/*
		 * TEXT = <any OCTET except CTLs, but including LWS>
		 */
		if ((types.contains(Type.OCTET) && !types.contains(Type.CTL)) || types.contains(Type.LWS)) {
			types.add(Type.TEXT);
		}
		
		HttpLexUnit unit = new HttpLexUnit(byteCursor, index - byteCursor, types);
		
		byteCursor = index;
		
		return unit;
	}
	
	public String getAsString(int index, int length) throws IllegalArgumentException {
		if (bytes == null) {
			return null;
		}
		if ((index < 0) || ((index+length) > bytes.length)) {
			throw new IllegalArgumentException();
		}

		return new String(Arrays.copyOfRange(bytes, index, index+length), HttpLexUnit.US_ASCII_CHARSET);
	}	
	
	public char getAsChar(HttpLexUnit unit) {
		if ((bytes == null) || (unit == null)) {
			return 0;
		}
		return (char)bytes[unit.getIndex()];		
	}
}