package cz.cvut.felk.rest.todo.core.method;

import java.io.InputStream;
import java.util.Map;

import cz.cvut.felk.rest.todo.core.Request;
import cz.cvut.felk.rest.todo.core.Response;
import cz.cvut.felk.rest.todo.core.content.ContentAdapter;
import cz.cvut.felk.rest.todo.errors.ErrorException;

public interface MethodDescriptor<I, O> {

	Map<String, ContentAdapter<InputStream, I>> consumes();
	Map<String, ContentAdapter<O, InputStream>> produces();
	
	Response<O> invoke(Request<I> request) throws ErrorException;
	//ContentDescriptor<O> invoke(String uri, Method method, ContentDescriptor<I> content) throws ErrorException;
	//Response<?> invoke(Request<?> request);
}