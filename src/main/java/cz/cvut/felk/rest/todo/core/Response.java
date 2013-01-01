package cz.cvut.felk.rest.todo.core;

import cz.cvut.felk.rest.todo.core.content.ContentDescriptor;


public interface Response<T> {

	int getStatus();
	ContentDescriptor<T> getContent();

	String getUri();
	String getContext();
}
