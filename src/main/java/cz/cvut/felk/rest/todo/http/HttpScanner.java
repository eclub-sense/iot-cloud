package cz.cvut.felk.rest.todo.http;

import java.util.Set;
import java.util.Stack;

public class HttpScanner {
 
	public enum LexType {
		OCTET, CHAR, UPALPHA, LOALPHA, ALPHA, DIGIT, CTL, CR, LF, SP, HT, DQM, CRLF, LWS, TEXT, HEX
	}
	
	private final String value;
	private final byte[] bytes;
//	private final Stack<Integer> stack;
	
	
	public HttpScanner(final String value) {
		super();
		this.value = value;
		this.bytes = (value != null) ? value.getBytes(HttpLang.US_ASCII_CHARSET) : null;
		//this.stack = new 
	}
	
	public LexUnit read() {
		
	}
	
	public LexUnit lookAhead() {
		
	}
	
//	public boolean isEnd() {
//		
//	}
//	public int getOffset() {
//		
//	}
//	
	
	public class LexUnit {
		
		private final byte[] value;
		private Set<LexType> types;
		
	}
	
}
