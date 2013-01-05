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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class HttpLexUnit {

	public static final Charset US_ASCII_CHARSET = Charset.forName("US-ASCII");

 	public enum Type {
		OCTET, CHAR, UPALPHA, LOALPHA, ALPHA, DIGIT, CTL, CR, LF, SP, HT, DQM, CRLF, LWS, TEXT, HEX, SEPARATOR
	}
	
	private final int index;
	private final int length;
	private final Set<Type> types;

	public HttpLexUnit(int index,int length, Set<Type> types) {
		super();
		this.index = index;
		this.length = length;
		this.types = types;
	}

	public int getIndex() {
		return index;
	}

	public Set<Type> getTypes() {
		return types;
	}
	
	public int getLength() {
		return length;
	}
	
	public boolean isType(Type type) {
		return (types != null) && types.contains(type);
	}		
	
	@Override
	public String toString() {
		return "[" + index + "," + length + ";" + ((types != null) ? Arrays.toString(types.toArray()) : "null") + "]";
	}
	
	/**
     * CHAR           = &lt;any US-ASCII character (octets 0 - 127)&gt; 
	 */	
	public static boolean isChar(byte value) {
		return (value >= 0) && (value <= 127);
	}

	/**
	 * CTL            = &lt;any US-ASCII control character (octets 0 - 31) and DEL (127)&gt;
	 */
	public static boolean isCtl(byte value) {
		return ((value >=0) && (value <= 31)) || (value == 127);
	}

	/**
     * SP             = &lt;US-ASCII SP, space (32)&gt;
	 */
	public static boolean isSp(byte value) {
		return value == 32;
	}
	
	/**
     * HT             = &lt;US-ASCII HT, horizontal-tab (9)&gt;
	 */
	public static boolean isHt(byte value) {
		return value == 9;
	}
	
	/**
     * separators     = "(" | ")" | "<" | ">" | "@"
     *                | "," | ";" | ":" | "\" | <">
     *                | "/" | "[" | "]" | "?" | "="
     *                | "{" | "}" | SP | HT
	 */
	public static boolean isSeparator(byte value) {
		return isSp(value) || isHt(value)
				|| ('(' == value) || (')' == value)
				|| ('<' == value) || ('>' == value)
				|| ('@' == value) || (',' == value) || (';' == value) || (':' == value)
				|| ('\\' == value) || ('"' == value) || ('/' == value) 
				|| ('[' == value) || (']' == value)
				|| ('?' == value) || ('=' == value)
				|| ('{' == value) || ('}' == value)
				; 
	}
	
    /**
	 * CR             = &lt;US-ASCII CR, carriage return (13)&gt;
	 */
	public static boolean isCr(byte value) {
		return value == 13;
	}
	
	/**
     * LF             = &lt;US-ASCII LF, linefeed (10)&gt;
	 */
	public static boolean isLf(byte value) {
		return value == 10;
	}	
	
	/**
	 * DIGIT          = &lt;any US-ASCII digit "0".."9"&gt; 
	 */
	public static boolean isDigit(byte value) {
		return ('0' <= value) && (value <= '9');
	}

	/**
	 * HEX            = "A" | "B" | "C" | "D" | "E" | "F"
     *                | "a" | "b" | "c" | "d" | "e" | "f" | DIGIT
	 */
	public static boolean isHex(byte value) {
		return isDigit(value) || (('A' <= value) && (value <= 'F')) || (('a' <= value) && (value <= 'f'));
	}
	
	/**
	 * UPALPHA        = &lt;any US-ASCII uppercase letter "A".."Z"&gt; 
	 */
	public static boolean isUpAlpha(byte value) {
		return ('A' <= value) && (value <= 'Z');
	}

	/**
	 * LOALPHA        = &lt;any US-ASCII lowercase letter "a".."z"&gt;
	 */
	public static boolean isLoAlpha(byte value) {
		return ('a' <= value) && (value <= 'z');
	}

	/**
	 * ALPHA          = UPALPHA | LOALPHA
	 */
	public static boolean isAlpha(byte value) {
		return isLoAlpha(value) || isUpAlpha(value);
	}

	/**
	 * token          = 1*&lt;any CHAR except CTLs or separators&gt;
	 */
	public static String readToken(HttpLexScanner scanner) throws IllegalArgumentException {
		if (scanner == null) {
			throw new IllegalArgumentException("The 'scanner' parameter must not be a null.");
		}

		scanner.tx();
		
		List<HttpLexUnit> units = new ArrayList<HttpLexUnit>();
		HttpLexUnit unit = null;
		do {
			if (unit != null) {
				units.add(unit);
			}
			scanner.commit();
			scanner.tx();
			unit = scanner.read();
		} while (unit != null && unit.isType(Type.CHAR) && !unit.isType(Type.CTL) && !unit.isType(Type.SEPARATOR));
					
		scanner.rollback();
		
		if (units.isEmpty()) {
			return null;
		}
		
		int index = units.iterator().next().getIndex();
		int length = 0;
		
		for (HttpLexUnit u : units) {
			length += u.getLength();
		}
		
		return scanner.getAsString(index, length);
	}
	
	/**
     * quoted-string  = ( &lt;"&gt; *(qdtext | quoted-pair ) &lt;"&gt; )
     *  qdtext        = &lt;any TEXT except &lt;"&gt;&gt;
     *  quoted-pair   = "\" CHAR
     */
	public static String readQuotedString(HttpLexScanner scanner) throws IllegalArgumentException {
		if (scanner == null) {
			throw new IllegalArgumentException("The 'scanner' parameter must not be a null.");
		}

		scanner.tx();
		
		List<HttpLexUnit> units = new ArrayList<HttpLexUnit>();
		
		HttpLexUnit unit = scanner.read();
		if ((unit == null) || ('"' != scanner.getAsChar(unit))) {
			scanner.rollback();
			return null;
		}

		scanner.tx();
		do {
			units.add(unit);

			scanner.commit();
			scanner.tx();
			unit = scanner.read();
			
			if ((unit != null) && ('\\' == scanner.getAsChar(unit))) {
				scanner.tx();
				HttpLexUnit tmpUnit = scanner.read();
				if ((tmpUnit != null) && (tmpUnit.isType(HttpLexUnit.Type.CHAR))) {
					units.add(unit);
					units.add(tmpUnit);
					scanner.commit();
					
					unit = scanner.read();
					
				} else {
					scanner.rollback();
				}
			}
			
		} while (unit != null && unit.isType(Type.CHAR) && ('"' != scanner.getAsChar(unit)));

		scanner.rollback();
		
		if (('"' != scanner.getAsChar(unit))) {
			scanner.rollback();
			return null;
		}
		scanner.commit();
		units.add(unit);
		
		int index = units.iterator().next().getIndex();
		int length = 0;
		
		for (HttpLexUnit u : units) {
			length += u.getLength();
		}
		
		return scanner.getAsString(index, length);		
	}

//	public static void skipWs(HttpScanner scanner) {
//		if (scanner == null) {
//			throw new IllegalArgumentException("The 'scanner' parameter must not be a null.");
//		}
//		
//		scanner.tx();
//		
//		HttpLexUnit unit = null;
//		do {
//			scanner.commit();
//			unit = scanner.read();
//			scanner.tx();
//		
//		} while (unit != null && unit.isType(Type.SP));
//					
//		scanner.rollback();
//	}	
}
