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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import cz.cvut.felk.rest.todo.core.ResponseHolder;
import cz.cvut.felk.rest.todo.core.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.dto.TodoListItemDto;
import cz.cvut.felk.rest.todo.errors.ErrorException;
import cz.cvut.felk.rest.todo.service.TodoListService;

public class TodoListServiceImpl implements TodoListService {

	private ConcurrentHashMap<String, ContentDescriptor<TodoItemDto>> memcache = new ConcurrentHashMap<String, ContentDescriptor<TodoItemDto>>();
	
	public ContentDescriptor<TodoItemDto> create(ContentDescriptor<TodoItemDto> content) throws ErrorException {

		if ((content == null) || (content.getBody() == null) || !content.getBody().validate()) {
			throw new ErrorException(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		ResponseHolder<TodoItemDto> item = new ResponseHolder<TodoItemDto>();
//		item.setUri(content.getUri());
//		item.setContext(content.getContext());
		
		Calendar cal = Calendar.getInstance();  
		cal.setTime(new Date());  
		cal.set(Calendar.MILLISECOND, 0);  
		
		item.setMeta(ContentDescriptor.META_LAST_MODIFIED, cal.getTime());
		item.setBody(content.getBody());
		
//		memcache.put(item.getUri(), item);
//		
//		return item;
		return null;
	}

	public ContentDescriptor<TodoItemDto> delete(ContentDescriptor<Void> content) {
//		ContentDescriptor<TodoItemDto> item = memcache.get(content.getUri());
//		if (item != null) {
//			if (memcache.remove(content.getUri(), item)) {
//				return item;
//			}
//		}
//		return item;
		return null;
	}

	public ContentDescriptor<TodoListItemDto[]> list(ContentDescriptor<Void> content) {
		List<TodoListItemDto> items = new ArrayList<TodoListItemDto>();
		
		for (ContentDescriptor<TodoItemDto> item : memcache.values()) {
			TodoListItemDto listItem = new TodoListItemDto();
			listItem.setDescription(item.getBody().getDescription());
			listItem.setLastModified(((Date)item.getMeta(ContentDescriptor.META_LAST_MODIFIED)).toGMTString());
			listItem.setState(item.getBody().getState());
//			listItem.setUri(content.getContext() + item.getUri());
			items.add(listItem);
		}
		
		ResponseHolder<TodoListItemDto[]> list = new ResponseHolder<TodoListItemDto[]>();
//		list.setUri(content.getUri());
//		list.setContext(content.getContext());
//		list.setBody(items.toArray(new TodoListItemDto[0]));
//		return list;
		return null;
		
	}

	public ContentDescriptor<TodoItemDto> read(ContentDescriptor<Void> content) {
//		return memcache.get(content.getUri());
		return null;
	}

	public ContentDescriptor<TodoItemDto> update(ContentDescriptor<TodoItemDto> content) throws ErrorException {
		
		if ((content == null) || (content.getBody() == null) || !content.getBody().validate()) {
			throw new ErrorException(HttpServletResponse.SC_BAD_REQUEST);
		}
		
//		ResponseHolder<TodoItemDto> item = (ResponseHolder<TodoItemDto>) memcache.get(content.getUri());
//		if (item == null) {
//			item = new ResponseHolder<TodoItemDto>();
//			item.setUri(content.getUri());
//		}
//		
//		Calendar cal = Calendar.getInstance();  
//		cal.setTime(new Date());  
//		cal.set(Calendar.MILLISECOND, 0);  
//		
//		item.setMeta(ContentDescriptor.META_LAST_MODIFIED, cal.getTime());
//		item.setBody(content.getBody());
		
//		memcache.put(item.getUri(), item);
		
//		return item;
		return null;
	}
}
