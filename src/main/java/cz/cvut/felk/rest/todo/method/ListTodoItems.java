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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sprintapi.api.ErrorException;
import org.sprintapi.api.Request;
import org.sprintapi.api.Response;
import org.sprintapi.api.ResponseHolder;
import org.sprintapi.api.content.ContentAdapter;
import org.sprintapi.api.content.ContentDescriptor;
import org.sprintapi.api.content.ContentHolder;
import org.sprintapi.api.method.MethodDescriptor;

import cz.cvut.felk.rest.todo.dao.TodoListDao;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.dto.TodoListItemDto;
import cz.cvut.felk.rest.todo.json.JsonMediaType;
import cz.cvut.felk.rest.todo.json.JsonSerializer;

public class ListTodoItems implements MethodDescriptor<Void, TodoListItemDto[]> {

	private Map<String, ContentAdapter<TodoListItemDto[], InputStream>> produces = new HashMap<String, ContentAdapter<TodoListItemDto[],InputStream>>();
	
	private final TodoListDao dao;
	
	public ListTodoItems(TodoListDao dao) {
		super();
		this.dao = dao;
		
		JsonSerializer<TodoListItemDto[]> serializer = new JsonSerializer<TodoListItemDto[]>();
		
		produces.put(JsonMediaType.LIST, serializer);
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
		
		for (String uri : dao.list()) {
			ContentDescriptor<TodoItemDto> item = dao.read(uri);
			TodoListItemDto listItem = new TodoListItemDto();
			listItem.setDescription(item.getBody().getDescription());
			listItem.setLastModified(((Date)item.getMeta(ContentDescriptor.META_LAST_MODIFIED)).toGMTString());
			listItem.setState(item.getBody().getState());
			listItem.setUri(request.getContext() + uri);
			items.add(listItem);
		}
		
		ContentHolder<TodoListItemDto[]> content = new ContentHolder<TodoListItemDto[]>();
		content.setBody(items.toArray(new TodoListItemDto[0]));
		
		ResponseHolder<TodoListItemDto[]> list = new ResponseHolder<TodoListItemDto[]>();
		list.setUri(request.getUri());
		list.setContext(request.getContext());
		list.setContent(content);
		return list;
	}
}
