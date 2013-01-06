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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cz.cvut.felk.rest.todo.api.ErrorException;
import cz.cvut.felk.rest.todo.api.Request;
import cz.cvut.felk.rest.todo.api.Response;
import cz.cvut.felk.rest.todo.api.ResponseHolder;
import cz.cvut.felk.rest.todo.api.content.ContentAdapter;
import cz.cvut.felk.rest.todo.api.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.api.method.MethodDescriptor;
import cz.cvut.felk.rest.todo.dao.TodoListDao;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.json.JsonMediaType;
import cz.cvut.felk.rest.todo.json.JsonSerializer;

public class ReadTodoItem implements MethodDescriptor<Void, TodoItemDto> {

	private Map<String, ContentAdapter<TodoItemDto, InputStream>> produces = new HashMap<String, ContentAdapter<TodoItemDto,InputStream>>();
	
	private final TodoListDao dao;
	
	public ReadTodoItem(TodoListDao dao) {
		super();
		this.dao = dao;
		
		JsonSerializer<TodoItemDto> serializer = new JsonSerializer<TodoItemDto>();
		
		produces.put(JsonMediaType.ITEM, serializer);
		produces.put(JsonMediaType.COMMON, serializer);
	}
	
	@Override
	public Map<String, ContentAdapter<InputStream, Void>> consumes() {
		return null;
	}

	@Override
	public Map<String, ContentAdapter<TodoItemDto, InputStream>> produces() {
		return produces;
	}

	@Override
	public Response<TodoItemDto> invoke(Request<Void> request) throws ErrorException {
				
		ContentDescriptor<TodoItemDto> content = dao.read(request.getUri());

		if (content != null) {
			ResponseHolder<TodoItemDto> response = new ResponseHolder<TodoItemDto>();
			response.setContent(content);

			Date ifModifiedSince = (Date) request.getContent().getMeta(ContentDescriptor.META_IF_MODIFIED_SINCE);
			Date lastModified = (Date) content.getMeta(ContentDescriptor.META_LAST_MODIFIED);
			
			if ((ifModifiedSince != null) && (ifModifiedSince.equals(lastModified) || lastModified.before(ifModifiedSince))) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return response;
			}
			return response;
		}
		throw new ErrorException(HttpServletResponse.SC_NOT_FOUND);
	}
}
