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
package cz.cvut.felk.rest.todo.dao;

import java.util.Collection;

import cz.cvut.felk.rest.todo.api.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;

public interface TodoListDao {

	ContentDescriptor<TodoItemDto> persist(String uri, ContentDescriptor<TodoItemDto> content);
	
	ContentDescriptor<TodoItemDto> read(String uri);
	
	ContentDescriptor<TodoItemDto> delete(String uri);
	
	Collection<String> list();
}
