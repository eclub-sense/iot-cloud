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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.sprintapi.api.ErrorException;
import org.sprintapi.api.content.ContentAdapter;

import com.google.gson.Gson;


public class JsonSerializer<T> implements ContentAdapter<T, InputStream>{

	@Override
	public InputStream transform(String uri, T in) throws ErrorException {
		return new ByteArrayInputStream((new Gson()).toJson(in).getBytes());
	}
}
