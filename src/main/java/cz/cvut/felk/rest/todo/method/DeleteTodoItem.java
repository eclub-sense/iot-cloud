package cz.cvut.felk.rest.todo.method;

import java.io.InputStream;
import java.util.Map;

import cz.cvut.felk.rest.todo.core.Request;
import cz.cvut.felk.rest.todo.core.Response;
import cz.cvut.felk.rest.todo.core.content.ContentAdapter;
import cz.cvut.felk.rest.todo.core.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.core.method.MethodDescriptor;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.errors.ErrorException;

public class DeleteTodoItem implements MethodDescriptor<Void, Void> {

	@Override
	public Map<String, ContentAdapter<InputStream, Void>> consumes() {
		return null;
	}

	@Override
	public Map<String, ContentAdapter<Void, InputStream>> produces() {
		return null;
	}

	@Override
	public Response<Void> invoke(Request<Void> request) throws ErrorException {

//		ContentDescriptor<TodoItemDto> item = memcache.get(content.getUri());
		
		if (item != null) {
			if (memcache.remove(content.getUri(), item)) {
				return item;
			}
		}

		return null;
	}

}
