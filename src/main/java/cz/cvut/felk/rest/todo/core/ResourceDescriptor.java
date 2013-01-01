package cz.cvut.felk.rest.todo.core;

import java.util.Map;

import cz.cvut.felk.rest.todo.core.method.Method;
import cz.cvut.felk.rest.todo.core.method.MethodDescriptor;


public interface ResourceDescriptor {

	Map<Method, MethodDescriptor<Object, Object>> methods();

}