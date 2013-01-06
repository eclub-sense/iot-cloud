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
package cz.cvut.felk.rest.todo.api.method;

import java.io.InputStream;
import java.util.Map;

import cz.cvut.felk.rest.todo.api.ErrorException;
import cz.cvut.felk.rest.todo.api.Request;
import cz.cvut.felk.rest.todo.api.Response;
import cz.cvut.felk.rest.todo.api.content.ContentAdapter;

public interface MethodDescriptor<I, O> {

	Map<String, ContentAdapter<InputStream, I>> consumes();
	Map<String, ContentAdapter<O, InputStream>> produces();
	
	Response<O> invoke(Request<I> request) throws ErrorException;
}