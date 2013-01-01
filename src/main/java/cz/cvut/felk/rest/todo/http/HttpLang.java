package cz.cvut.felk.rest.todo.http;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Stack;

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
     * CRLF           = CR LF
	 */
	public static boolean isCrLf(byte value) {
		return isC;
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
	
	public static String parseToken(ParserContext context) throws ParseException, IllegalArgumentException {
		if (context == null) {
			
		}
		if (context)
		
	}
	
	public class ParserContext {
		
		private byte[] bytes;
		private Stack<Integer> stack;
		
		
	}
}
