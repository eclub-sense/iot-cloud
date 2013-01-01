package cz.cvut.felk.rest.todo.core.content;



public interface ContentDescriptor<T> {

	static final String CT_ITEM = "application/vnd.todo.item+json";
	static final String CT_LIST = "application/vnd.todo.list+json";
	static final String CT_JSON = "application/json";
	
	final static String META_CONTENT_TYPE = "Content-Type";
	final static String META_LOCATION = "Location";
	final static String META_LAST_MODIFIED = "Last-Modified";
	final static String META_IF_MODIFIED_SINCE = "If-Modified-Since";

	Object getMeta(String name);
	
	T getBody();
	
	String[] getMetaNames();

}
