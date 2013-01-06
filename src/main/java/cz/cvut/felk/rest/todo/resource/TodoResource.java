package cz.cvut.felk.rest.todo.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cz.cvut.felk.rest.todo.core.ResourceDescriptor;
import cz.cvut.felk.rest.todo.dao.TodoListDao;
import cz.cvut.felk.rest.todo.http.method.Method;
import cz.cvut.felk.rest.todo.http.method.MethodDescriptor;
import cz.cvut.felk.rest.todo.method.DeleteTodoItem;

public class TodoResource implements ResourceDescriptor {

	private final Map<Method, MethodDescriptor<?, ?>> methods = new ConcurrentHashMap<Method, MethodDescriptor<?, ?>>();
	
	private final TodoListDao dao;

	public TodoResource(TodoListDao dao) {
		super();
		this.dao = dao;
		methods.put(Method.DELETE, new DeleteTodoItem(dao));
	}

	@Override
	public Map<Method, MethodDescriptor<?, ?>> methods() {
		return methods;
	}

}
