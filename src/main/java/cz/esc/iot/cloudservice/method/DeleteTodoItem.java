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
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.sprintapi.api.ErrorException;
import org.sprintapi.api.Request;
import org.sprintapi.api.Response;
import org.sprintapi.api.ResponseHolder;
import org.sprintapi.api.content.ContentAdapter;
import org.sprintapi.api.content.ContentDescriptor;
import org.sprintapi.api.method.MethodDescriptor;

import cz.esc.iot.cloudservice.dao.TodoListDao;
import cz.esc.iot.cloudservice.dto.TodoItemDto;

public class DeleteTodoItem implements MethodDescriptor<Void, Void> {

	private final TodoListDao dao;
	
	public DeleteTodoItem(TodoListDao dao) {
		super();
		this.dao = dao;
	}
	
	@Override
	public Map<String, ContentAdapter<InputStream, Void>> consumes() {
		return null;
	}

	@Override
	public Map<String, ContentAdapter<Void, InputStream>> produces() {
		return null;
	}

	@Override
	public Response<Void> invoke(Request<Void> request) throws ErrorException {


		ContentDescriptor<TodoItemDto> item = dao.delete(request.getUri());
		
		if (item == null) {
			throw new ErrorException(request.getUri(), HttpServletResponse.SC_NOT_FOUND);
		}

		ResponseHolder<Void> response = new ResponseHolder<Void>();
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		return response;
	}

}
