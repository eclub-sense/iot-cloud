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
package cz.cvut.felk.rest.todo.api;

import cz.cvut.felk.rest.todo.api.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.api.method.Method;

public class RequestHolder<T> implements Request<T> {

	private Method method;
	private String uri;
	private String context;
	private ContentDescriptor<T> content; 
	
	public RequestHolder(String uri) {
		super();
		this.uri = uri;
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}

	public void setContent(ContentDescriptor<T> content) {
		this.content = content;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public ContentDescriptor<T> getContent() {
		return content;
	}

	@Override
	public String getContext() {
		return context;
	}

	
	public void setContext(String context) {
		this.context = context;
	}

	
}