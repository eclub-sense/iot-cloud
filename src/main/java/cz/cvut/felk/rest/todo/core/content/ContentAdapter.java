package cz.cvut.felk.rest.todo.core.content;

import cz.cvut.felk.rest.todo.errors.ErrorException;

public interface ContentAdapter<A, B> {

	B transform(A in) throws ErrorException;
}
