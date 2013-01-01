package cz.cvut.felk.rest.todo.core;

import cz.cvut.felk.rest.todo.core.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.core.method.Method;


public interface Request<T> {

	String getUri();
	Method getMethod();
	
	ContentDescriptor<T> getContent();
}
