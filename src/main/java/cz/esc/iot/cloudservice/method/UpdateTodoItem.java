/*
 *  Copyright 2012 the original author or authors.
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
package cz.esc.iot.cloudservice.method;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.sprintapi.api.ErrorException;
import org.sprintapi.api.Request;
import org.sprintapi.api.Response;
import org.sprintapi.api.ResponseHolder;
import org.sprintapi.api.content.ContentAdapter;
import org.sprintapi.api.content.ContentDescriptor;
import org.sprintapi.api.content.ContentHolder;
import org.sprintapi.api.method.MethodDescriptor;

import cz.esc.iot.cloudservice.dao.TodoListDao;
import cz.esc.iot.cloudservice.dto.TodoItemDto;
import cz.esc.iot.cloudservice.json.JsonDeserializer;
import cz.esc.iot.cloudservice.json.JsonMediaType;
import cz.esc.iot.cloudservice.json.JsonSerializer;

public class UpdateTodoItem implements MethodDescriptor<TodoItemDto, TodoItemDto> {

	private Map<String, ContentAdapter<InputStream, TodoItemDto>> consumes = new HashMap<String, ContentAdapter<InputStream,TodoItemDto>>();
	private Map<String, ContentAdapter<TodoItemDto, InputStream>> produces = new HashMap<String, ContentAdapter<TodoItemDto,InputStream>>();
	
	private final TodoListDao dao;
	
	public UpdateTodoItem(TodoListDao dao) {
		super();
		this.dao = dao;
		
		JsonDeserializer<TodoItemDto> deserializer = new JsonDeserializer<TodoItemDto>(TodoItemDto.class);
		JsonSerializer<TodoItemDto> serializer = new JsonSerializer<TodoItemDto>();
		
		consumes.put(JsonMediaType.ITEM, deserializer);
		consumes.put(JsonMediaType.COMMON, deserializer);
		
		produces.put(JsonMediaType.ITEM, serializer);
		produces.put(JsonMediaType.COMMON, serializer);
	}
	
	@Override
	public Map<String, ContentAdapter<InputStream, TodoItemDto>> consumes() {
		return consumes;
	}

	@Override
	public Map<String, ContentAdapter<TodoItemDto, InputStream>> produces() {
		return produces;
	}

	@Override
	public Response<TodoItemDto> invoke(Request<TodoItemDto> request) throws ErrorException {

		if (dao.read(request.getUri()) == null) {
			throw new ErrorException(request.getUri(), HttpServletResponse.SC_NOT_FOUND);
		}
		

		if ((request == null) || (request.getContent() == null) || (request.getContent().getBody() == null) || !request.getContent().getBody().validate()) {
			throw new ErrorException(request.getUri(), HttpServletResponse.SC_BAD_REQUEST);
		}

		Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.MILLISECOND, 0);  
	
		ContentHolder<TodoItemDto> content = new ContentHolder<TodoItemDto>();
		content.setMeta(ContentDescriptor.META_LAST_MODIFIED, cal.getTime());
		content.setBody(request.getContent().getBody());
		
		ResponseHolder<TodoItemDto> response = new ResponseHolder<TodoItemDto>();
		response.setUri(request.getUri());
		response.setContext(request.getContext());
		response.setContent(content);
		
		dao.persist(response.getUri(), content);
		return response;
	}
}
