package cz.cvut.felk.rest.todo.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cz.cvut.felk.rest.todo.core.ResourceDescriptor;
import cz.cvut.felk.rest.todo.http.method.Method;
import cz.cvut.felk.rest.todo.http.method.MethodDescriptor;
import cz.cvut.felk.rest.todo.method.CreateTodoItem;

public class TodoListResource implements ResourceDescriptor {

	private static final Map<Method, MethodDescriptor<?, ?>> METHODS = new ConcurrentHashMap<Method, MethodDescriptor<?, ?>>();
	
	static {
		METHODS.put(Method.POST, new CreateTodoItem());
	}
	
	@Override
	public Map<Method, MethodDescriptor<?, ?>> methods() {
		return METHODS;
	}
}
