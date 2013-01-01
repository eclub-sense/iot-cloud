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
			if ((cursor + 1) < bytes.length) {
				if (isLf(bytes[cursor])) {
					types.add(LexType.CRLF);
					index += 1;
				} else {
					types.add(LexType.CR);
				}
			} else {
				types.add(LexType.CR);
			}
		}
		if (isCtl(bytes[cursor])) {
			types.add(LexType.CTL);
		}
		if (isHt(bytes[cursor])) {
			types.add(LexType.HT);
		}
		if (isLf(bytes[cursor])) {
			types.add(LexType.LF);
		}
		if (isSeparator(bytes[cursor])) {
			types.add(LexType.SEPARATOR);
		}
		if (isSp(bytes[cursor])) {
			types.add(LexType.SP);
		}
		index += 1;

		byte[] value = new byte[index-cursor];
		for (int i=0; i < (index-cursor); i++) {
			value[i] = bytes[cursor+i];
		}
		
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
		return isSp(value) || isHt(value); 
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
     * TEXT           = &lt;any OCTET except CTLs, but including LWS&gt;
	 */

	/**
     * LWS            = [CRLF] 1*( SP | HT )
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
	
	public class LexUnit {
		
		private final byte[] value;
		private Set<LexType> types;
	
		public LexUnit(byte[] value, Set<LexType> types) {
			super();
			this.value = value;
			this.types = types;
		}
	}
	
}
