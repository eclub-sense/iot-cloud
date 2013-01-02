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
package cz.cvut.felk.rest.todo.core;

import cz.cvut.felk.rest.todo.core.method.Method;

public class RequestHolder<T> implements Request<T> {

	private Method method;
	
	public RequestHolder(String uri) {
		super();
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}

	@Override
	public Object getMeta(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBody(T body) {
		
	}
	
	public void setUri(String uri) {
		
	}
	
	@Override
	public T getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMetaNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Method getMethod() {
		return method;
	}

}
