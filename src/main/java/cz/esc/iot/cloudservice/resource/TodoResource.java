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
package cz.esc.iot.cloudservice.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sprintapi.api.ResourceDescriptor;
import org.sprintapi.api.method.DefaultOptionsMethod;
import org.sprintapi.api.method.Method;
import org.sprintapi.api.method.MethodDescriptor;

import cz.esc.iot.cloudservice.dao.TodoListDao;
import cz.esc.iot.cloudservice.method.DeleteTodoItem;
import cz.esc.iot.cloudservice.method.ReadMetaTodoItem;
import cz.esc.iot.cloudservice.method.ReadTodoItem;
import cz.esc.iot.cloudservice.method.UpdateTodoItem;

public class TodoResource implements ResourceDescriptor {

	private final Map<Method, MethodDescriptor<?, ?>> methods = new ConcurrentHashMap<Method, MethodDescriptor<?, ?>>();
	
	public TodoResource(TodoListDao dao) {
		super();
		methods.put(Method.GET, new ReadTodoItem(dao));
		methods.put(Method.DELETE, new DeleteTodoItem(dao));
		methods.put(Method.PUT, new UpdateTodoItem(dao));
		methods.put(Method.HEAD, new ReadMetaTodoItem(dao));
		methods.put(Method.OPTIONS, new DefaultOptionsMethod(this));
	}

	@Override
	public Map<Method, MethodDescriptor<?, ?>> methods() {
		return methods;
	}

}
