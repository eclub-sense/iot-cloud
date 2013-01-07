/*
 *  Copyright 2012 the original author or authors.
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
package org.sprintapi.api.method;

import java.io.InputStream;
import java.util.Map;

import org.sprintapi.api.ErrorException;
import org.sprintapi.api.Request;
import org.sprintapi.api.ResourceDescriptor;
import org.sprintapi.api.Response;
import org.sprintapi.api.ResponseHolder;
import org.sprintapi.api.content.ContentAdapter;
import org.sprintapi.api.content.ContentDescriptor;
import org.sprintapi.api.content.ContentHolder;


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
		ContentHolder<Void> content = new ContentHolder<Void>();
		content.setMeta(ContentDescriptor.META_ALLOW, allow);
		
		response.setContent(content);
		response.setUri(request.getUri());
		
		return response;
	}
}
