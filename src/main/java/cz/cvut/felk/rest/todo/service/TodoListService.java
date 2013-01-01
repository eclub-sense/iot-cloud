package cz.cvut.felk.rest.todo.service;

import cz.cvut.felk.rest.todo.core.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.dto.TodoListItemDto;
import cz.cvut.felk.rest.todo.errors.ErrorException;

public interface TodoListService {

	ContentDescriptor<TodoItemDto> create(ContentDescriptor<TodoItemDto> ContentDescriptor) throws ErrorException;
	
	ContentDescriptor<TodoItemDto> update(ContentDescriptor<TodoItemDto> ContentDescriptor) throws ErrorException;
	
	ContentDescriptor<TodoItemDto> delete(ContentDescriptor<Void> ContentDescriptor);

	ContentDescriptor<TodoListItemDto[]> list(ContentDescriptor<Void> ContentDescriptor);

	ContentDescriptor<TodoItemDto> read(ContentDescriptor<Void> ContentDescriptor);
	
}
