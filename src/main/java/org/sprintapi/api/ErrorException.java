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
package org.sprintapi.api;


public class ErrorException extends Exception {

	private static final long serialVersionUID = 1531552972478130773L;
	
	private final int statusCode;
	private final String uri;

	public ErrorException(String uri, int statusCode) {
		this(uri, statusCode, null);
	}
	
	public ErrorException(String uri, int statusCode, Throwable cause) {
		super(cause);
		this.uri = uri;
		this.statusCode = statusCode;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	@Override
	public String getMessage() {
		return "ErrorException (code=" + statusCode + ", uri=" + uri + ")" + ((getCause() != null) ? " [" + getCause().getMessage() + "]" : "" );
	}
}
