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
package org.sprintapi.api.content;

import java.util.HashMap;
import java.util.Map;



public class ContentHolder<T> implements ContentDescriptor<T> {

	private T body;
	private Map<String, Object> meta;
	
	public ContentHolder() {
		super();
		this.meta = new HashMap<String, Object>();
	}
	
	@Override
	public Object getMeta(String name) {
		return meta.get(name);
	}

	@Override
	public T getBody() {
		return body;
	}

	@Override
	public String[] getMetaNames() {
		return meta.keySet().toArray(new String[0]);
	}

	public void setMeta(String name, Object value) {
		meta.put(name, value);
	}

	public void setBody(T body) {
		this.body = body;
	}
}
