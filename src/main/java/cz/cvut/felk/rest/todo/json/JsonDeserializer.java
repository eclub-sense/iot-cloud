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
package cz.cvut.felk.rest.todo.json;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import cz.cvut.felk.rest.todo.api.ErrorException;
import cz.cvut.felk.rest.todo.api.content.ContentAdapter;

public class JsonDeserializer<T> implements ContentAdapter<InputStream, T>{

	private final Class<? extends T> clazz;
	
	public JsonDeserializer(Class<? extends T> clazz) {
		super();
		this.clazz = clazz;
	}
	
	@Override
	public T transform(String uri, InputStream in) throws ErrorException {
		try {
		    return (new Gson()).fromJson(new InputStreamReader(in), clazz);
		    			
		} catch (JsonSyntaxException e) {
			throw new ErrorException(uri, HttpServletResponse.SC_BAD_REQUEST, e);
			
		} catch (JsonIOException e) {
			throw new ErrorException(uri, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);			
		}
	}
}
