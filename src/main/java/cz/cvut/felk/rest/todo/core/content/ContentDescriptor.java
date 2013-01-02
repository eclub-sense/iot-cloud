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
package cz.cvut.felk.rest.todo.core.content;



public interface ContentDescriptor<T> {

	static final String CT_ITEM = "application/vnd.todo.item+json";
	static final String CT_LIST = "application/vnd.todo.list+json";
	static final String CT_JSON = "application/json";
	
	final static String META_CONTENT_TYPE = "Content-Type";
	final static String META_LOCATION = "Location";
	final static String META_LAST_MODIFIED = "Last-Modified";
	final static String META_IF_MODIFIED_SINCE = "If-Modified-Since";

	Object getMeta(String name);
	
	T getBody();
	
	String[] getMetaNames();

}
