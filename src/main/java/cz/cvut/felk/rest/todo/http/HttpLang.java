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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cz.cvut.felk.rest.todo.http.HttpScanner.LexType;
import cz.cvut.felk.rest.todo.http.HttpScanner.LexUnit;

/**
 * The following rules are used throughout HTTP/1.1 specification to
 * describe basic parsing constructs. The US-ASCII coded character set
 * is defined by ANSI X3.4-1986.
 *
 */
public class HttpLang {

	public static final Charset US_ASCII_CHARSET = Charset.forName("US-ASCII");

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
	public static boolean isToken(String value) {
		if (value != null) {
			byte[] bytes =  value.trim().getBytes(US_ASCII_CHARSET);
			if (bytes != null) {
				for (byte b : bytes) {
					if (!isChar(b) || isCtl(b) || isSeparator(b)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}	

	/**
	 * token          = 1*&lt;any CHAR except CTLs or separators&gt;
	 */
	public static String readToken(HttpScanner scanner) throws ParseException, IllegalArgumentException {
		if (scanner == null) {
			throw new IllegalArgumentException("The 'scanner' parameter must not be a null.");
		}

		List<LexUnit> units = new ArrayList<LexUnit>();
		LexUnit unit = null;
		do {
			if (unit != null) {
				units.add(unit);
			}
			unit = scanner.read();
		
		} while (unit != null && unit.isType(LexType.CHAR) && !unit.isType(LexType.CTL) && !unit.isType(LexType.SEPARATOR));
					
		if (unit != null) {
			scanner.rollback(1);
		}
		
		if (units.isEmpty()) {
			return null;
		}
		
		int index = units.iterator().next().getIndex();
		int length = 0;
		
		for (LexUnit u : units) {
			length += u.getLength();
		}
		
		return scanner.getAsString(index, length);
	}
	
	public class ParserContext {
		
		private byte[] bytes;
		private Stack<Integer> stack;
		
		
	}
}
