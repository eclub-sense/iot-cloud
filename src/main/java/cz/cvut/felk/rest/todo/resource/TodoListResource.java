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
package cz.cvut.felk.rest.todo.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sprintapi.api.ResourceDescriptor;
import org.sprintapi.api.method.DefaultOptionsMethod;
import org.sprintapi.api.method.Method;
import org.sprintapi.api.method.MethodDescriptor;

import cz.cvut.felk.rest.todo.dao.TodoListDao;
import cz.cvut.felk.rest.todo.method.CreateTodoItem;
import cz.cvut.felk.rest.todo.method.ListTodoItems;

public class TodoListResource implements ResourceDescriptor {

	private final Map<Method, MethodDescriptor<?, ?>> methods = new ConcurrentHashMap<Method, MethodDescriptor<?, ?>>();

	public TodoListResource(TodoListDao dao) {
		super();
		methods.put(Method.OPTIONS, new DefaultOptionsMethod(this));
		methods.put(Method.POST, new CreateTodoItem(dao));
		methods.put(Method.GET, new ListTodoItems(dao));
	}
	
	@Override
	public Map<Method, MethodDescriptor<?, ?>> methods() {
		return methods;
	}
}
