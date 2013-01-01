package cz.cvut.felk.rest.todo.json;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.google.gson.Gson;

import cz.cvut.felk.rest.todo.core.content.ContentAdapter;
import cz.cvut.felk.rest.todo.errors.ErrorException;

public class JsonSerializer<T> implements ContentAdapter<T, InputStream>{

	@Override
	public InputStream transform(T in) throws ErrorException {
		return new ByteArrayInputStream((new Gson()).toJson(in).getBytes());
	}
}
