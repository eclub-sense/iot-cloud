package cz.cvut.felk.rest.todo.http;

import java.text.ParseException;

public class HttpMediaRange extends HttpMediaType {

	public static final String ANY = "*";
	
	public static final HttpMediaRange ALL_MEDIA_TYPES = new HttpMediaRange(ANY, ANY, null); 
		
	public HttpMediaRange(String type, String subtype, HttpParameter[] parameters) {
		super(type, subtype, parameters);
	}

	public static HttpMediaRange valueOf(String mediaRange) throws ParseException, IllegalArgumentException {
		return null;
	}

	public boolean match(final HttpMediaType mediaType) {		
		return isValid() 
				&& (ANY.equals(type) 
						|| (type.equals(mediaType.getType()) && (ANY.equals(subtype) || subtype.equals(mediaType.getSubtype()))));
	}
	
	public boolean isValid() {
		return super.isValid() && ((ANY.equals(type) && ANY.equals(subtype)) || !ANY.equals(type));
	}
	
}
