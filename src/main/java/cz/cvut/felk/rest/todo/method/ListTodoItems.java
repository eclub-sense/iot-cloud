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
package cz.cvut.felk.rest.todo.method;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cvut.felk.rest.todo.core.Request;
import cz.cvut.felk.rest.todo.core.Response;
import cz.cvut.felk.rest.todo.core.ResponseHolder;
import cz.cvut.felk.rest.todo.core.content.ContentAdapter;
import cz.cvut.felk.rest.todo.core.method.MethodDescriptor;
import cz.cvut.felk.rest.todo.dto.TodoListItemDto;
import cz.cvut.felk.rest.todo.errors.ErrorException;
import cz.cvut.felk.rest.todo.json.JsonMediaType;
import cz.cvut.felk.rest.todo.json.JsonSerializer;

public class ListTodoItems implements MethodDescriptor<Void, TodoListItemDto[]> {

	private Map<String, ContentAdapter<TodoListItemDto[], InputStream>> produces = new HashMap<String, ContentAdapter<TodoListItemDto[],InputStream>>();
	
	public ListTodoItems() {
		super();
		
		JsonSerializer<TodoListItemDto[]> serializer = new JsonSerializer<TodoListItemDto[]>();
		
	//	produces.put(JsonMediaType.ITEMS, serializer);
		produces.put(JsonMediaType.COMMON, serializer);
	}

	@Override
	public Map<String, ContentAdapter<InputStream, Void>> consumes() {
		return null;
	}

	@Override
	public Map<String, ContentAdapter<TodoListItemDto[], InputStream>> produces() {
		return produces;
	}

	@Override
	public Response<TodoListItemDto[]> invoke(Request<Void> request) throws ErrorException {
		
		List<TodoListItemDto> items = new ArrayList<TodoListItemDto>();
		
//		for (ContentDescriptor<TodoItemDto> item : memcache.values()) {
//			TodoListItemDto listItem = new TodoListItemDto();
//			listItem.setDescription(item.getBody().getDescription());
//			listItem.setLastModified(((Date)item.getMeta(ContentDescriptor.META_LAST_MODIFIED)).toGMTString());
//			listItem.setState(item.getBody().getState());
//			listItem.setUri(content.getContext() + item.getUri());
//			items.add(listItem);
//		}
		
		ResponseHolder<TodoListItemDto[]> list = new ResponseHolder<TodoListItemDto[]>();
//		list.setUri(content.getUri());
//		list.setContext(content.getContext());
//		list.setBody(items.toArray(new TodoListItemDto[0]));
		return list;
	}
}
