package cz.cvut.felk.rest.todo.dao;

import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.dto.TodoListItemDto;
import cz.cvut.felk.rest.todo.http.content.ContentDescriptor;

public interface TodoListDao {

	ContentDescriptor<TodoItemDto> persist(String uri, ContentDescriptor<TodoItemDto> content);
	
	ContentDescriptor<TodoItemDto> read(String uri);
	
	ContentDescriptor<TodoItemDto> delete(String uri);
	
	ContentDescriptor<TodoListItemDto[]> list();
}
