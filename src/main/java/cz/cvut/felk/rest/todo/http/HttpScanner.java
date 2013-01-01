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
	private int byteCursor;
	
	private final LexUnit[] units;
	private int unitCursor;
	
	
	public HttpScanner(final String value) {
		super();
		this.value = value;
		this.bytes = (value != null) ? value.getBytes(HttpLang.US_ASCII_CHARSET) : null;
		this.units = (bytes != null) ? new LexUnit[bytes.length] : null;
		this.byteCursor = 0;
		this.unitCursor = 0;
	}
	
	public LexUnit read() {
		if (units[unitCursor] != null) {
			units[unitCursor] = scan();
			if (units[unitCursor] == null) {
				return null;
			}
		}

		unitCursor += 1;
		return units[unitCursor -1];
	}

	protected LexUnit scan() {
		if ((bytes == null) || (byteCursor >= bytes.length)) {
			return null;
		}

		int index = byteCursor;
		Set<LexType> types = new HashSet<HttpScanner.LexType>();
		
		if (isChar(bytes[byteCursor])) {
			types.add(LexType.CHAR);
		}
		if (isCr(bytes[byteCursor])) {
			types.add(LexType.CR);
			if (((byteCursor + 1) < bytes.length) && isLf(bytes[byteCursor+1])) {
				types.add(LexType.LF);
				types.add(LexType.CRLF);
				index += 1;

				if ((byteCursor + 2) < bytes.length) {
					if (isHt(bytes[byteCursor+2])) {
						types.add(LexType.HT);
						types.add(LexType.LWS);
						index += 1;
						
					} else if (isSp(bytes[byteCursor+2])) {
						types.add(LexType.SP);
						types.add(LexType.LWS);
						index += 1;
					}	
				}
			}
		}
		if (isCtl(bytes[byteCursor])) {
			types.add(LexType.CTL);
		}
		if (isHt(bytes[byteCursor])) {
			types.add(LexType.HT);
			types.add(LexType.LWS);
		}
		if (isLf(bytes[byteCursor])) {
			types.add(LexType.LF);
		}
		if (isSeparator(bytes[byteCursor])) {
			types.add(LexType.SEPARATOR);
		}
		if (isSp(bytes[byteCursor])) {
			types.add(LexType.SP);
			types.add(LexType.LWS);
		}
		if (isAlpha(bytes[byteCursor])) {
			types.add(LexType.ALPHA);
		}
		if (isUpAlpha(bytes[byteCursor])) {
			types.add(LexType.UPALPHA);
		}		
		if (isLoAlpha(bytes[byteCursor])) {
			types.add(LexType.LOALPHA);
		}
		if (isDigit(bytes[byteCursor])) {
			types.add(LexType.DIGIT);
		}
		if (isHex(bytes[byteCursor])) {
			types.add(LexType.HEX);
		}
		if (bytes[byteCursor] == 34) {
			types.add(LexType.DQM);
		}
		
		index += 1;

		byte[] value = new byte[index-byteCursor];
		for (int i=0; i < (index-byteCursor); i++) {
			value[i] = bytes[byteCursor+i];
		}
		
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
		byteCursor = index;
		
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