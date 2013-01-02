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
package cz.cvut.felk.rest.todo.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.cvut.felk.rest.todo.core.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.errors.ErrorException;
import cz.cvut.felk.rest.todo.service.ServiceAdapter;
import cz.cvut.felk.rest.todo.service.TodoListService;
import cz.cvut.felk.rest.todo.servlet.HttpRequest;
import cz.cvut.felk.rest.todo.servlet.HttpResponse;

public class ServiceAdapterImpl implements ServiceAdapter {
	
	protected static final String URL_ITEM = "/item/";
	
	protected static final Set<String> urls;
	
	static {
		urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
		urls.add(URL_ITEM);
	}
	
	protected TodoListService todoListSvc;
	
	public ServiceAdapterImpl() {
		super();
		this.todoListSvc = new TodoListServiceImpl();
	}
	
	public void create(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException {

		final String url = HttpRequest.getUri(httpRequest);
		 
		ContentDescriptor<?> content = null;
		
		if (URL_ITEM.equals(url)) {

			content = todoListSvc.create(
					HttpRequest.read(
							TodoItemDto.class, 
							httpRequest,
							URL_ITEM + Long.toHexString((new Date()).getTime())
						)
					);
		}

		if (content != null) {
			HttpResponse.write(HttpServletResponse.SC_CREATED, content, httpResponse);
			return;
		}

		if (urls.contains(url)) {
			throw new ErrorException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		throw new ErrorException(HttpServletResponse.SC_NOT_FOUND);		
	}
	
	public void read(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException {
		
		final String url = HttpRequest.getUri(httpRequest);
		final HttpRequest<Void> request = HttpRequest.read(Void.class, httpRequest, url);

		ContentDescriptor<?> content = null;
		
		if (URL_ITEM.equals(url)) {
			
			content = todoListSvc.list(request);

		} else if ((url != null) && url.startsWith(URL_ITEM)) {

			content = todoListSvc.read(request);

		} else if (urls.contains(url)) {
			httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}

		if (content != null) {
			Date ifModifiedSince = (Date) request.getMeta(ContentDescriptor.META_IF_MODIFIED_SINCE);
			Date lastModified = (Date) content.getMeta(ContentDescriptor.META_LAST_MODIFIED);
			
			if ((ifModifiedSince != null) && (ifModifiedSince.equals(lastModified) || lastModified.before(ifModifiedSince))) {
				HttpResponse.write(HttpServletResponse.SC_NOT_MODIFIED, content, httpResponse);
				return;
			}
			
			HttpResponse.write(HttpServletResponse.SC_OK, content, httpResponse);
			return;
		}	
		throw new ErrorException(HttpServletResponse.SC_NOT_FOUND);
	}
	
	public void update(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException {
		
		final String url = HttpRequest.getUri(httpRequest);
		 
		ContentDescriptor<?> content = null;
		
		if ((url != null) && url.startsWith(URL_ITEM)) {

			content = todoListSvc.update(
					HttpRequest.read(
							TodoItemDto.class, 
							httpRequest,
							url
						)
					);
		}

		if (content != null) {
			HttpResponse.write(HttpServletResponse.SC_OK, content, httpResponse);
			return;
		}

		if (urls.contains(url)) {
			throw new ErrorException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		throw new ErrorException(HttpServletResponse.SC_NOT_FOUND);		
	}
	
	public void delete(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException {
		
		final String url = HttpRequest.getUri(httpRequest);
		
		ContentDescriptor<?> content = null;
		
		if ((url != null) && url.startsWith(URL_ITEM)) {

			content = todoListSvc.delete(
					HttpRequest.read(
							Void.class, 
							httpRequest,
							url
						)
					);
		}
		
		if (content != null) {
			HttpResponse.write(HttpServletResponse.SC_NO_CONTENT, content, httpResponse);
		}
		
		httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}	
}