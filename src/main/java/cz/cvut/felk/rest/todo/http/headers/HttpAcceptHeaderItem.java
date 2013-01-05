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
package cz.cvut.felk.rest.todo.http.headers;



public class HttpAcceptHeaderItem {

	private final HttpMediaRange range;
	private final Float qualityFactor;
	private final HttpAcceptExtension[] extensions;
	
	public HttpAcceptHeaderItem(HttpMediaRange range, Float qualityFactor, HttpAcceptExtension[] extensions) {
		super();
		this.range = range;
		this.qualityFactor = qualityFactor;
		this.extensions = extensions;
	}

	public HttpMediaRange getRange() {
		return range;
	}

	public Float getQualityFactor() {
		return qualityFactor;
	}

	public HttpAcceptExtension[] getExtensions() {
		return extensions;
	}
}
