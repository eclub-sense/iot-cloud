/*
 *  Copyright 2012 sprintapi.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cz.cvut.felk.rest.todo.service;

import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.dto.TodoListItemDto;
import cz.cvut.felk.rest.todo.http.ErrorException;
import cz.cvut.felk.rest.todo.http.content.ContentDescriptor;

public interface TodoListService {

	ContentDescriptor<TodoItemDto> create(ContentDescriptor<TodoItemDto> ContentDescriptor) throws ErrorException;
	
	ContentDescriptor<TodoItemDto> update(ContentDescriptor<TodoItemDto> ContentDescriptor) throws ErrorException;
	
	ContentDescriptor<TodoItemDto> delete(ContentDescriptor<Void> ContentDescriptor);

	ContentDescriptor<TodoListItemDto[]> list(ContentDescriptor<Void> ContentDescriptor);

	ContentDescriptor<TodoItemDto> read(ContentDescriptor<Void> ContentDescriptor);
	
}
