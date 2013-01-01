package cz.cvut.felk.rest.todo.core.content;

import cz.cvut.felk.rest.todo.dto.TodoItemDto;


public class LocalContent<T> implements ContentDescriptor<T> {

	@Override
	public Object getMeta(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMetaNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMeta(String name, Object value) {
		// TODO Auto-generated method stub
		
	}

	public void setBody(T body) {
		// TODO Auto-generated method stub
		
	}
}
