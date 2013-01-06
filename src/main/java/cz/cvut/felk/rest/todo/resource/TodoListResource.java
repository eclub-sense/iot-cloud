package cz.cvut.felk.rest.todo.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cz.cvut.felk.rest.todo.core.ResourceDescriptor;
import cz.cvut.felk.rest.todo.dao.TodoListDao;
import cz.cvut.felk.rest.todo.http.method.Method;
import cz.cvut.felk.rest.todo.http.method.MethodDescriptor;
import cz.cvut.felk.rest.todo.method.CreateTodoItem;
import cz.cvut.felk.rest.todo.method.ListTodoItems;

public class TodoListResource implements ResourceDescriptor {

	private final Map<Method, MethodDescriptor<?, ?>> methods = new ConcurrentHashMap<Method, MethodDescriptor<?, ?>>();

	public TodoListResource(TodoListDao dao) {
		super();
		methods.put(Method.POST, new CreateTodoItem(dao));
		methods.put(Method.GET, new ListTodoItems(dao));
	}
	
	@Override
	public Map<Method, MethodDescriptor<?, ?>> methods() {
		return methods;
	}
}
