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
package org.sprintapi.api.method;

import java.io.InputStream;
import java.util.Map;

import org.sprintapi.api.ErrorException;
import org.sprintapi.api.Request;
import org.sprintapi.api.Response;
import org.sprintapi.api.content.ContentAdapter;


public interface MethodDescriptor<I, O> {

	Map<String, ContentAdapter<InputStream, I>> consumes();
	Map<String, ContentAdapter<O, InputStream>> produces();
	
	Response<O> invoke(Request<I> request) throws ErrorException;
}