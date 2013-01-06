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
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cz.cvut.felk.rest.todo.core.Request;
import cz.cvut.felk.rest.todo.core.Response;
import cz.cvut.felk.rest.todo.core.ResponseHolder;
import cz.cvut.felk.rest.todo.dao.TodoListDao;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.http.ErrorException;
import cz.cvut.felk.rest.todo.http.content.ContentAdapter;
import cz.cvut.felk.rest.todo.http.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.http.method.MethodDescriptor;
import cz.cvut.felk.rest.todo.http.servlet.HttpRequest;
import cz.cvut.felk.rest.todo.http.servlet.HttpResponse;

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
			throw new ErrorException(HttpServletResponse.SC_NOT_FOUND);
		}

		ResponseHolder<Void> response = new ResponseHolder<Void>();
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		return response;
	}

}
