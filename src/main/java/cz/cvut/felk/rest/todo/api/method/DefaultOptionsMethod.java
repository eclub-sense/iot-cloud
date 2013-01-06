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
import cz.cvut.felk.rest.todo.api.ResourceDescriptor;
import cz.cvut.felk.rest.todo.api.Response;
import cz.cvut.felk.rest.todo.api.ResponseHolder;
import cz.cvut.felk.rest.todo.api.content.ContentAdapter;
import cz.cvut.felk.rest.todo.api.content.ContentDescriptor;

public class DefaultOptionsMethod implements MethodDescriptor<Void, Void> {

	private final ResourceDescriptor resource;
	
	public DefaultOptionsMethod(ResourceDescriptor resource) {
		super();
		this.resource = resource;
	}
	
	@Override
	public Map<String, ContentAdapter<InputStream, Void>> consumes() {
		return null;
	}

	@Override
	public Map<String, ContentAdapter<Void, InputStream>> produces() {
		return null;
	}

	@Override
	public Response<Void> invoke(Request<Void> request) throws ErrorException {
		
		// Set Allow header - no response content
		ResponseHolder<Void> response = new ResponseHolder<Void>();

		String allow = Method.OPTIONS.name();
			
		if (resource.methods().keySet() != null) {
			for (Method method : resource.methods().keySet()) {
				if (!Method.OPTIONS.equals(method)) {
					allow += ", " + method.name();					
				}
			}
		}
		response.setMeta(ContentDescriptor.META_ALLOW, allow);
		return response;
	}
}
