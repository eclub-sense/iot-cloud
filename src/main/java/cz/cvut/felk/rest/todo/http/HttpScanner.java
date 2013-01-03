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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class HttpScanner {

 	public enum LexType {
		OCTET, CHAR, UPALPHA, LOALPHA, ALPHA, DIGIT, CTL, CR, LF, SP, HT, DQM, CRLF, LWS, TEXT, HEX, SEPARATOR
	}
	
	private final String value;
	private final byte[] bytes;
	private int byteCursor;
	
	private final LexUnit[] units;
	private int unitCursor;
	
	private Stack<Integer[]> tx = new Stack<Integer[]>();
	
	
	public HttpScanner(final String value) {
		super();
		this.value = value;
		this.bytes = (value != null) ? value.getBytes(HttpLang.US_ASCII_CHARSET) : null;
		this.units = (bytes != null) ? new LexUnit[bytes.length] : null;
		this.byteCursor = 0;
		this.unitCursor = 0;
	}
	
	public LexUnit read() {
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
		tx.push(new Integer[]{unitCursor, byteCursor});
	}
	
	public void commit() {
		tx.pop();
	}
	
	public void rollback() {
		Integer[] t = tx.pop();
			
		unitCursor = t[0];
	}

	protected LexUnit scan() {
		if ((bytes == null) || (byteCursor >= bytes.length)) {
			return null;
		}

		int index = byteCursor;
		Set<LexType> types = new HashSet<HttpScanner.LexType>();
		
		if (HttpLang.isChar(bytes[byteCursor])) {
			types.add(LexType.CHAR);
		}
		if (HttpLang.isCr(bytes[byteCursor])) {
			types.add(LexType.CR);
			if (((byteCursor + 1) < bytes.length) && HttpLang.isLf(bytes[byteCursor+1])) {
				types.add(LexType.LF);
				types.add(LexType.CRLF);
				index += 1;

				if ((byteCursor + 2) < bytes.length) {
					if (HttpLang.isHt(bytes[byteCursor+2])) {
						types.add(LexType.HT);
						types.add(LexType.LWS);
						index += 1;
						
					} else if (HttpLang.isSp(bytes[byteCursor+2])) {
						types.add(LexType.SP);
						types.add(LexType.LWS);
						index += 1;
					}	
				}
			}
		}
		if (HttpLang.isCtl(bytes[byteCursor])) {
			types.add(LexType.CTL);
		}
		if (HttpLang.isHt(bytes[byteCursor])) {
			types.add(LexType.HT);
			types.add(LexType.LWS);
		}
		if (HttpLang.isLf(bytes[byteCursor])) {
			types.add(LexType.LF);
		}
		if (HttpLang.isSeparator(bytes[byteCursor])) {
			types.add(LexType.SEPARATOR);
		}
		if (HttpLang.isSp(bytes[byteCursor])) {
			types.add(LexType.SP);
			types.add(LexType.LWS);
		}
		if (HttpLang.isAlpha(bytes[byteCursor])) {
			types.add(LexType.ALPHA);
		}
		if (HttpLang.isUpAlpha(bytes[byteCursor])) {
			types.add(LexType.UPALPHA);
		}		
		if (HttpLang.isLoAlpha(bytes[byteCursor])) {
			types.add(LexType.LOALPHA);
		}
		if (HttpLang.isDigit(bytes[byteCursor])) {
			types.add(LexType.DIGIT);
		}
		if (HttpLang.isHex(bytes[byteCursor])) {
			types.add(LexType.HEX);
		}
		if (bytes[byteCursor] == 34) {
			types.add(LexType.DQM);
		}
		
		index += 1;

		/*
		 * OCTET = <any 8-bit sequence of data>
		 */
		if ((index - byteCursor) == 1) {
			types.add(LexType.OCTET);
		}

		/*
		 * TEXT = <any OCTET except CTLs, but including LWS>
		 */
		if ((types.contains(LexType.OCTET) && !types.contains(LexType.CTL)) || types.contains(LexType.LWS)) {
			types.add(LexType.TEXT);
		}
		
		LexUnit unit = new LexUnit(byteCursor, index - byteCursor, types);
		
		byteCursor = index;
		
		return unit;
	}
	
//	public LexUnit lookAhead() {
//		
//	}
	
//	public boolean isEnd() {
//		
//	}
//	public int getOffset() {
//		
//	}
//	
	public String getAsString(int index, int length) throws IllegalArgumentException {
		if (bytes == null) {
			return null;
		}
		if ((index < 0) || ((index+length) > bytes.length)) {
			throw new IllegalArgumentException();
		}

		return new String(Arrays.copyOfRange(bytes, index, index+length), HttpLang.US_ASCII_CHARSET);
	}	
	
	public Character getAsChar(LexUnit unit) {
		if (bytes == null) {
			return null;
		}
		return (char)bytes[unit.getIndex()];		
	}
	
//	public int lexUnits() {
//		return unitCursor;
//	}
	
	/**
     * quoted-string  = ( &lt;"&gt; *(qdtext | quoted-pair ) &lt;"&gt; )
     */

	/**
     *  qdtext         = &lt;any TEXT except &lt;"&gt;&gt;
	 */

	/**
     * quoted-pair    = "\" CHAR
	 */

	
	public class LexUnit {
		
		private final int index;
		private final int length;
		private final Set<LexType> types;
	
		public LexUnit(int index,int length, Set<LexType> types) {
			super();
			this.index = index;
			this.length = length;
			this.types = types;
		}

		public int getIndex() {
			return index;
		}

		public Set<LexType> getTypes() {
			return types;
		}
		
		public int getLength() {
			return length;
		}
		
		public boolean isType(LexType type) {
			return (types != null) && types.contains(type);
		}		
		
		@Override
		public String toString() {
			return "[" + index + "," + length + ";" + ((types != null) ? Arrays.toString(types.toArray()) : "null") + "]";
		}
	}
}