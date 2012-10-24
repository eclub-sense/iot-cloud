package cz.cvut.felk.rest.todo.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import cz.cvut.felk.rest.todo.ContentDescriptor;
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
		
		LocalContentDescriptor<TodoItemDto> item = new LocalContentDescriptor<TodoItemDto>();
		item.setUrl(content.getUrl());
		item.setContextUrl(content.getContextUrl());
		
		Calendar cal = Calendar.getInstance();  
		cal.setTime(new Date());  
		cal.set(Calendar.MILLISECOND, 0);  
		
		item.setMeta(ContentDescriptor.META_LAST_MODIFIED, cal.getTime());
		item.setBody(content.getBody());
		
		memcache.put(item.getUrl(), item);
		
		return item;
	}

	public ContentDescriptor<TodoItemDto> delete(ContentDescriptor<Void> content) {
		ContentDescriptor<TodoItemDto> item = memcache.get(content.getUrl());
		if (item != null) {
			if (memcache.remove(content.getUrl(), item)) {
				return item;
			}
		}
		return item;
	}

	public ContentDescriptor<TodoListItemDto[]> list(ContentDescriptor<Void> content) {
		List<TodoListItemDto> items = new ArrayList<TodoListItemDto>();
		
		for (ContentDescriptor<TodoItemDto> item : memcache.values()) {
			TodoListItemDto listItem = new TodoListItemDto();
			listItem.setDescription(item.getBody().getDescription());
			listItem.setLastModified(((Date)item.getMeta(ContentDescriptor.META_LAST_MODIFIED)).toGMTString());
			listItem.setState(item.getBody().getState());
			listItem.setUrl(content.getContextUrl() + item.getUrl());
			items.add(listItem);
		}
		
		LocalContentDescriptor<TodoListItemDto[]> list = new LocalContentDescriptor<TodoListItemDto[]>();
		list.setUrl(content.getUrl());
		list.setContextUrl(content.getContextUrl());
		list.setBody(items.toArray(new TodoListItemDto[0]));
		return list;
		
	}

	public ContentDescriptor<TodoItemDto> read(ContentDescriptor<Void> content) {
		return memcache.get(content.getUrl());
	}

	public ContentDescriptor<TodoItemDto> update(ContentDescriptor<TodoItemDto> content) throws ErrorException {
		
		if ((content == null) || (content.getBody() == null) || !content.getBody().validate()) {
			throw new ErrorException(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		LocalContentDescriptor<TodoItemDto> item = (LocalContentDescriptor<TodoItemDto>) memcache.get(content.getUrl());
		if (item == null) {
			item = new LocalContentDescriptor<TodoItemDto>();
			item.setUrl(content.getUrl());
		}
		
		Calendar cal = Calendar.getInstance();  
		cal.setTime(new Date());  
		cal.set(Calendar.MILLISECOND, 0);  
		
		item.setMeta(ContentDescriptor.META_LAST_MODIFIED, cal.getTime());
		item.setBody(content.getBody());
		
		memcache.put(item.getUrl(), item);
		
		return item;
	}
}
