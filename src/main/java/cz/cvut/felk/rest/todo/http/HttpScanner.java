package cz.cvut.felk.rest.todo.http;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class HttpScanner {

	public static final Charset US_ASCII_CHARSET = Charset.forName("US-ASCII");
	
	public enum LexType {
		OCTET, CHAR, UPALPHA, LOALPHA, ALPHA, DIGIT, CTL, CR, LF, SP, HT, DQM, CRLF, LWS, TEXT, HEX, SEPARATOR
	}
	
	private final String value;
	private final byte[] bytes;
	private int cursor;
	
	
	public HttpScanner(final String value) {
		super();
		this.value = value;
		this.bytes = (value != null) ? value.getBytes(HttpLang.US_ASCII_CHARSET) : null;
		this.cursor = 0;
	}
	
	public LexUnit read() {
		if ((bytes == null) || (cursor >= bytes.length)) {
			return null;
		}
		 
		int index = cursor;
		Set<LexType> types = new HashSet<HttpScanner.LexType>();
		
		if (isChar(bytes[cursor])) {
			types.add(LexType.CHAR);
		}
		if (isCr(bytes[cursor])) {
			types.add(LexType.CR);
			if (((cursor + 1) < bytes.length) && isLf(bytes[cursor+1])) {
				types.add(LexType.LF);
				types.add(LexType.CRLF);
				index += 1;

				if ((cursor + 2) < bytes.length) {
					if (isHt(bytes[cursor+2])) {
						types.add(LexType.HT);
						types.add(LexType.LWS);
						index += 1;
						
					} else if (isSp(bytes[cursor+2])) {
						types.add(LexType.SP);
						types.add(LexType.LWS);
						index += 1;
					}	
				}
			}
		}
		if (isCtl(bytes[cursor])) {
			types.add(LexType.CTL);
		}
		if (isHt(bytes[cursor])) {
			types.add(LexType.HT);
			types.add(LexType.LWS);
		}
		if (isLf(bytes[cursor])) {
			types.add(LexType.LF);
		}
		if (isSeparator(bytes[cursor])) {
			types.add(LexType.SEPARATOR);
		}
		if (isSp(bytes[cursor])) {
			types.add(LexType.SP);
			types.add(LexType.LWS);
		}
		if (isAlpha(bytes[cursor])) {
			types.add(LexType.ALPHA);
		}
		if (isUpAlpha(bytes[cursor])) {
			types.add(LexType.UPALPHA);
		}		
		if (isLoAlpha(bytes[cursor])) {
			types.add(LexType.LOALPHA);
		}
		if (isDigit(bytes[cursor])) {
			types.add(LexType.DIGIT);
		}
		if (isHex(bytes[cursor])) {
			types.add(LexType.HEX);
		}
		if (bytes[cursor] == 34) {
			types.add(LexType.DQM);
		}
		
		index += 1;

		byte[] value = new byte[index-cursor];
		for (int i=0; i < (index-cursor); i++) {
			value[i] = bytes[cursor+i];
		}
		
		if ((index - cursor) == 1) {
			types.add(LexType.OCTET);
		}

		/*
		 *   TEXT           = <any OCTET except CTLs, but including LWS>
		 */
		if ((types.contains(LexType.OCTET) && !types.contains(LexType.CTL)) || types.contains(LexType.LWS)) {
			types.add(LexType.TEXT);
		}
		cursor = index;
		
		return new LexUnit(value, types);
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
     * quoted-string  = ( &lt;"&gt; *(qdtext | quoted-pair ) &lt;"&gt; )
     */

	/**
     *  qdtext         = &lt;any TEXT except &lt;"&gt;&gt;
	 */

	/**
     * quoted-pair    = "\" CHAR
	 */

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
	
	public class LexUnit {
		
		private final byte[] value;
		private final Set<LexType> types;
	
		public LexUnit(byte[] value, Set<LexType> types) {
			super();
			this.value = value;
			this.types = types;
		}

		public byte[] getValue() {
			return value;
		}

		public Set<LexType> getTypes() {
			return types;
		}
	}	
}