package cz.cvut.felk.rest.todo.http;

import java.text.ParseException;

public class HttpParameter {

	private final String attribute;
	private final String value;
	
	public HttpParameter(final String attribute, final String value) {
		super();
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * <ul>
	 *   <li>parameter               = attribute "=" value</li>
     *   <li>attribute               = token</li>
     *   <li>value                   = token | quoted-string</li>
     * </ul>
	 */
	public static HttpParameter valueOf(final String value) throws ParseException, IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException("The 'value' parameter cannot be a null.");
		}
		if (!value.contains("=")) {
			throw new ParseException(value, 0);	
		}
		
		String attribute = value.substring(0, value.indexOf("="));
		if (!HttpLang.isToken(attribute)) {
			throw new ParseException(value, 0);
		}
		
		String val = value.substring(value.indexOf("=") + 1);
		if (!HttpLang.isToken(val) && !HttpLang.isQuotedString(val)) {
			throw new ParseException(value, attribute.length());
		}
		return new HttpParameter(attribute.trim(), val.trim());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(attribute);
		builder.append('=');
		builder.append(value);
		return builder.toString();
	}
	
	public String getAttribute() {
		return attribute;
	}

	public String getValue() {
		return value;
	}
}
