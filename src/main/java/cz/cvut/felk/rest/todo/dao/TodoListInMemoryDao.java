package cz.cvut.felk.rest.todo.dao;

import java.util.concurrent.ConcurrentHashMap;

import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.dto.TodoListItemDto;
import cz.cvut.felk.rest.todo.http.content.ContentDescriptor;

public class TodoListInMemoryDao implements TodoListDao {

	private ConcurrentHashMap<String, ContentDescriptor<TodoItemDto>> memcache = new ConcurrentHashMap<String, ContentDescriptor<TodoItemDto>>();

	@Override
	public ContentDescriptor<TodoItemDto> persist(String uri, ContentDescriptor<TodoItemDto> content) {
		return memcache.put(uri, content);
	}

	@Override
	public ContentDescriptor<TodoItemDto> read(String uri) {
		return memcache.get(uri);
	}

	@Override
	public ContentDescriptor<TodoItemDto> delete(String uri) {
		return memcache.remove(uri);
	}

	@Override
	public ContentDescriptor<TodoListItemDto[]> list() {
		// TODO Auto-generated method stub
		return null;
	}
}
