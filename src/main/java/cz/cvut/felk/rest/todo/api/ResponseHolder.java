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

public class ResponseHolder<T> implements Response<T> {

	private String url;
	private String contextUrl;
	
	private ContentDescriptor<T> content;
	
	private int status;
	
	public String getUri() {
		return url;
	}

	public void setUri(String url) {
		this.url = url;
	}

	public String getContext() {
		return contextUrl;
	}

	public void setContext(String contextUrl) {
		this.contextUrl = contextUrl;
	}

	@Override
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public ContentDescriptor<T> getContent() {
		return content;
	}

	public void setContent(ContentDescriptor<T> content) {
		this.content = content;
	}
}
