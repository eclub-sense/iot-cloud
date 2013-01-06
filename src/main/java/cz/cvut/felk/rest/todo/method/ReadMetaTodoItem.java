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

public class ReadMetaTodoItem implements MethodDescriptor<Void, Void> {

	private final TodoListDao dao;
	
	public ReadMetaTodoItem(TodoListDao dao) {
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
				
		ContentDescriptor<TodoItemDto> content = dao.read(request.getUri());

		if (content != null) {
			ResponseHolder<Void> response = new ResponseHolder<Void>();
			
			ContentHolder<Void> meta = new ContentHolder<Void>();
			if (content.getMetaNames() != null) {
				for (String key : content.getMetaNames()) {
					meta.setMeta(key, content.getMeta(key));	
				}
			}
			response.setContent(meta);
			response.setUri(request.getUri());
			response.setContext(request.getContext());
			return response;
		}
		throw new ErrorException(request.getUri(), HttpServletResponse.SC_NOT_FOUND);
	}
}
